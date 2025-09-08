package kb_hack.backend.domain.chat.entity;

import kb_hack.backend.domain.common.Superclass.BaseTimeEntity;
import kb_hack.backend.domain.sos.entity.SosType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoom extends BaseTimeEntity {

	private Long chatRoomId;

	private String roomName;

	private Long sosId;

	private SosType roomType;

	private int isComplete; // 0: 진행중, 1: 완료

	private Long ownerId;
}
