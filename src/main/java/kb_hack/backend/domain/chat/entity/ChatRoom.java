package kb_hack.backend.domain.chat.entity;

import kb_hack.backend.common.Superclass.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoom extends BaseTimeEntity {

	private Long chatRoomId;

	private String name;

	private boolean isActive;

}
