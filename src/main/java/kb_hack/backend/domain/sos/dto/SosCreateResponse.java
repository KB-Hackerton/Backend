package kb_hack.backend.domain.sos.dto;

import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SosCreateResponse {
	private Long sosId;
	private List<String> imageKeys; // 저장된 storage_key 리스트
}
