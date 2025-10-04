package kb_hack.backend.domain.openAI.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class AiRecommendationRequestDto {
    private String model;
    private List< Map<String, String>> input;
    //role , content
    private float temperature;
}
