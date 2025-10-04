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
    private final BusinessMapper businessMapper; // 사업장 정보
    private final AnnounceMapper announceMapper; // 전체 공고 목록

    // DB 조회
    public Announce recommendAnnounce(Long memberId){
        BusinessPlus userBusiness = businessMapper.findBusinessAndClassInfoByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 사업장 정보를 찾을 수 없습니다."));

        List<Announce> activeAnnouncements = announceMapper.findAll();

        String businessInfo = String.format(
                "사용자 사업장 : %s , %s",
                userBusiness.getBusinessNm(),
                userBusiness.getBusinessAddr()
        );

        String announcesInfo = activeAnnouncements.stream()
                .map(a -> {
                    // DB 데이터가 길기 때문에, 공고 설명을 HTML 제거 후 50자 이내로 요약
                    String plainDesc = a.getDescription().replaceAll("<[^>]*>", "").replaceAll("\\s+", " ").trim();
                    if (plainDesc.length() > 50) {
                        plainDesc = plainDesc.substring(0, 50) + "...";
                    }
                    return String.format("ID:%d, 제목:%s, 요약설명:%s", a.getAnnounceId(), a.getAnnounceTitle(), plainDesc);
                })
                .collect(Collectors.joining("\n"));
        log.info(announcesInfo);

        String aiPrompt = String.join("\n",
                "사업장 정보", businessInfo,
                " 지원사업 목록 ", announcesInfo
        );

        String recommendedIdString = openAiService.getAiResponseId(aiPrompt);

        // 4. 최종 ID로 DB에서 공고 객체 조회 및 반환
        if (recommendedIdString != null) {
            try {
                Long finalId = Long.parseLong(recommendedIdString.trim());
                return announceMapper.findById2(finalId) // 최종 공고 조회
                        .orElse(null);
            } catch (NumberFormatException e) {
                log.error("AI 응답이 유효한 ID 숫자가 아닙니다: {}", recommendedIdString, e);
            }
        }

        return null;
    }
}