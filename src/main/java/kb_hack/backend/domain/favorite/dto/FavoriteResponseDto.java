package kb_hack.backend.domain.favorite.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteResponseDto {
	private Long announceId;
	private String announceTitle;
}

