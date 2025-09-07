package kb_hack.backend.global.config.websocket.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;


import kb_hack.backend.domain.chat.service.ChatService;
import kb_hack.backend.global.util.JwtProcessor;
import lombok.extern.slf4j.Slf4j;

// 인증 작업
@Slf4j
@Component
public class StompHandler implements ChannelInterceptor {

	private final JwtProcessor jwtProcessor;
	private final ChatService chatService;

	public StompHandler(JwtProcessor jwtProcessor, ChatService chatService) {
		this.jwtProcessor = jwtProcessor;
		this.chatService = chatService;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			try {
				log.info("CONNECT 요청 처리 시작");
				String bearerToken = accessor.getFirstNativeHeader("Authorization");
				if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
					String token = bearerToken.substring(7);
					jwtProcessor.validateToken(token);
					log.info("CONNECT 토큰 검증 완료");
				} else {
					log.warn("CONNECT 시 토큰이 없거나 형식이 올바르지 않습니다.");
					// 필요하다면 여기서 예외를 던져 연결을 막을 수 있습니다.
				}
			} catch (Exception e) {
				log.error("CONNECT 처리 중 예외 발생", e);
				throw e;
			}
		}

		if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
			// ★★★★★ SUBSCRIBE 블록 전체를 try-catch로 감쌉니다 ★★★★★
			try {
				log.info("SUBSCRIBE 요청 처리 시작");
				String bearerToken = accessor.getFirstNativeHeader("Authorization");
				String destination = accessor.getDestination();

				// 어떤 값이 들어오는지 정확히 확인합니다.
				log.info("SUBSCRIBE 헤더의 Authorization: {}", bearerToken);
				log.info("SUBSCRIBE 헤더의 Destination: {}", destination);

				// 이제 null 체크를 합니다.
				if (bearerToken == null || destination == null) {
					// 여기서 에러의 원인이 되는 NullPointerException을 미리 방지합니다.
					log.error("Authorization 헤더 또는 Destination이 null입니다. 처리를 중단합니다.");
					// 적절한 예외를 던져줍니다.
					throw new AuthenticationServiceException("필수 헤더 또는 Destination 정보가 누락되었습니다.");
				}

				String token = bearerToken.substring(7);
				String email = jwtProcessor.getUserEmail(token);
				String roomId = destination.split("/")[2]; // 이 부분도 ArrayIndexOutOfBoundsException 가능성이 있습니다.

				if (!chatService.isRoomParticipant(email, Long.parseLong(roomId))) {
					throw new AuthenticationServiceException("해당 room에 권한이 없습니다.");
				}
				log.info("SUBSCRIBE 권한 검증 완료");

			} catch (Exception e) {
				// 여기서 발생하는 모든 예외를 잡아서 스택 트레이스를 출력합니다.
				log.error("!!!!!!!! SUBSCRIBE 처리 중 심각한 예외 발생 !!!!!!!!");
				log.error("예외 타입: {}", e.getClass().getSimpleName());
				log.error("예외 메시지: {}", e.getMessage());
				log.error("스택 트레이스:", e); // 가장 중요한 정보!
				throw e; // 예외를 다시 던져서 원래 흐름대로 처리
			}
		}

		return message;
	}
}
