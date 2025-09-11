package kb_hack.backend.domain.openAI.service;

import kb_hack.backend.domain.announce.Announce;
import kb_hack.backend.domain.announce.mapper.AnnounceMapper;
import kb_hack.backend.domain.business.BusinessPlus;
import kb_hack.backend.domain.business.mapper.BusinessMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiRecommendationService {

    private final AiService openAiService;
    private final BusinessMapper businessMapper;
    private final AnnounceMapper announceMapper;

    private <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }

    public Mono<Announce> recommendAnnounce(Long memberId) {
        // 1. 사용자 사업장 정보 조회 (Mono로 래핑)
        Mono<BusinessPlus> businessMono = Mono.fromSupplier(
                () -> businessMapper.findBusinessAndClassInfoByMemberId(memberId)
                        .orElseThrow(() -> new IllegalArgumentException("Business not found for memberId: " + memberId))
        );

        // 2. 공고 후보 목록 조회 (Mono로 래핑)
        Mono<List<Announce>> announcesMono = Mono.fromSupplier(() -> announceMapper.findAll());

        // 3. 두 Mono를 결합하여 비동기 로직 실행
        return Mono.zip(businessMono, announcesMono)
                .flatMap(tuple -> {
                    BusinessPlus userBusiness = tuple.getT1();
                    List<Announce> activeAnnouncements = tuple.getT2();

                    if (activeAnnouncements.isEmpty()) {
                        return Mono.empty();
                    }

                    String businessInfo = String.format(
                            "사용자 사업장 정보: 업종=%s(%s), 주소=%s",
                            userBusiness.getBusinessClassMinorName(),
                            userBusiness.getBusinessClassMiddleName(),
                            userBusiness.getBusinessAddr()
                    );

                    List<List<Announce>> partitions = partition(activeAnnouncements, 50);

                    // 4. 각 청크별로 GPT 호출 → 후보 ID 수집
                    return Flux.fromIterable(partitions)
                            .flatMap(part -> {
                                String announcesInfo = part.stream()
                                        .map(a -> {
                                            String plainDesc = a.getDescription()
                                                    .replaceAll("<[^>]*>", "")
                                                    .replaceAll("\\s+", " ")
                                                    .trim();
                                            if (plainDesc.length() > 30) {
                                                plainDesc = plainDesc.substring(0, 30) + "...";
                                            }
                                            return String.format(
                                                    "ID:%d, 제목:%s, 설명:%s",
                                                    a.getAnnounceId(), a.getAnnounceTitle(), plainDesc
                                            );
                                        })
                                        .collect(Collectors.joining("\n"));

                                String aiPrompt = String.join("\n",
                                        "### 사용자 사업장 정보",
                                        businessInfo,
                                        "### 지원사업 목록 (일부)",
                                        announcesInfo,
                                        "### 추천 공고 ID"
                                );

                                // 비동기 API 호출 및 재시도 로직 추가
                                return openAiService.askOpenAi(aiPrompt)
                                        .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)) // 5초 간격으로 3번 재시도
                                                .filter(throwable -> {
                                                    log.warn("API 호출 재시도: {}", throwable.getMessage());
                                                    return throwable instanceof Exception;
                                                }))
                                        .onErrorResume(e -> {
                                            log.error("⚠️ 청크 추천 실패: {}", e.getMessage());
                                            return Mono.empty(); // 실패 시 빈 Mono 반환
                                        });
                            })
                            .collectList()
                            .flatMap(candidateIdsString -> {
                                List<Long> candidateIds = candidateIdsString.stream()
                                        .map(Long::parseLong)
                                        .collect(Collectors.toList());

                                if (candidateIds.isEmpty()) {
                                    log.error("⚠️ 후보 ID를 뽑지 못했습니다.");
                                    return Mono.empty();
                                }

                                // 5. 후보 ID들을 다시 GPT에 던져 최종 선택
                                String candidateInfo = candidateIds.stream()
                                        .map(id -> announceMapper.findById2(id).orElse(null))
                                        .filter(a -> a != null)
                                        .map(a -> String.format("ID:%d, 제목:%s", a.getAnnounceId(), a.getAnnounceTitle()))
                                        .collect(Collectors.joining("\n"));

                                String finalPrompt = String.join("\n",
                                        "### 사용자 사업장 정보",
                                        businessInfo,
                                        "### 후보 공고 목록",
                                        candidateInfo,
                                        "### 최종 추천 공고 ID"
                                );

                                return openAiService.askOpenAi(finalPrompt)
                                        .flatMap(finalResponse -> {
                                            try {
                                                Long finalId = Long.parseLong(finalResponse.trim());
                                                return Mono.justOrEmpty(announceMapper.findById2(finalId));
                                            } catch (NumberFormatException e) {
                                                log.error("⚠️ 최종 AI 응답이 숫자가 아님: {}", finalResponse, e);
                                                return Mono.empty();
                                            }
                                        })
                                        .switchIfEmpty(Mono.error(new RuntimeException("추천된 공고를 찾을 수 없습니다.")));
                            });
                });
    }
}
