package kb_hack.backend.domain.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kb_hack.backend.domain.chat.mapper.ReadStatusMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReadStatusService {
	private final ReadStatusMapper readStatusMapper;

	// 메시지 읽음 상태 업데이트 메서드
	public void markMessagesAsRead(Long roomId, Long memberId) {
		int updatedCount = readStatusMapper.markAsRead(roomId, memberId);
		log.info("Updated {} messages as read in room {}", updatedCount, roomId);
	}

	public Long getUnreadCount(Long roomId, Long memberId) {
		return readStatusMapper.countUnreadMessages(roomId, memberId);
	}

}
