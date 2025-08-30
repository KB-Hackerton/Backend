package kb_hack.backend.domain.common.Superclass;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public abstract class BaseTimeEntity {
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
