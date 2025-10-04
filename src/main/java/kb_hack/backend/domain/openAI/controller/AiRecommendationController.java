package kb_hack.backend.domain.openAI.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kb_hack.backend.domain.announce.Announce;
import kb_hack.backend.domain.openAI.service.AiRecommendationService;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
@Tag(name = "AI 추천 API", description = "AI 기반으로 맞춤형 지원사업을 추천합니다.")
public class AiRecommendationController {

    private final AiRecommendationService aiRecommendationService;

    @Operation(
            summary = "AI 기반 공고 추천",
            description = "사용자 정보에 기반하여 AI가 가장 적합한 공고 1개를 추천합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public SuccessResponse<Announce> getRecommendedAnnounce(@RequestParam Long memberId) {
        // Service는 동기적으로 Announce 객체를 반환합니다.
        Announce recommendedAnnounce = aiRecommendationService.recommendAnnounce(memberId);

        if (recommendedAnnounce == null) {
            // 추천 실패 시 적절한 예외 또는 응답 처리
            throw new RuntimeException("AI 추천에 적합한 공고를 찾을 수 없습니다.");
        }

        return SuccessResponse.makeResponse(
                SuccessStatusCode.RECOMMENDATION_SUCCESS,
                recommendedAnnounce
        );
    }
}