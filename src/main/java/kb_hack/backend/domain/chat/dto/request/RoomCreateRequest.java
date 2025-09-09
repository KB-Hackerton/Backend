package kb_hack.backend.domain.chat.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomCreateRequest {
	private Long sosId;
	private Long otherMemberId;

}
