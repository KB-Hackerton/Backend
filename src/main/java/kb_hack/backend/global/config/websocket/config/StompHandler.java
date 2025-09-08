package kb_hack.backend.global.config.websocket.config;

import static kb_hack.backend.global.security.filter.JwtAuthenticationFilter.*;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import kb_hack.backend.domain.chat.service.ChatService;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.entity.MemberVO;
import kb_hack.backend.global.util.JwtProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtProcessor jwtProcessor;
    private final ChatService chatService;
    // UserDetailsService를 주입받아 사용자 정보를 조회합니다.
    private final UserDetailsService userDetailsService;

    // 생성자 수정
    public StompHandler(JwtProcessor jwtProcessor, ChatService chatService, UserDetailsService userDetailsService) {
        this.jwtProcessor = jwtProcessor;
        this.chatService = chatService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // StompCommand가 CONNECT일 때, WebSocket 연결 초기 인증 처리
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("STOMP CONNECT - User: {}", accessor.getUser());
        }

        // StompCommand가 SUBSCRIBE일 때, 특정 채팅방에 대한 구독 권한 검증
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {

            try {
                // accessor.getUser()를 통해 CONNECT 단계에서 저장한 인증 정보를 바로 가져올 수 있습니다.
                String bearerToken = accessor.getFirstNativeHeader("Authorization");
                Authentication authentication = (Authentication) accessor.getUser();
                log.info("SUBSCRIBE 요청 처리 시작, 인증된 사용자: {}", authentication != null ? authentication.getName() : "없음");
                if (authentication == null) {
                    throw new AuthenticationServiceException("인증 정보가 없습니다.");
                }
                log.info("SUBSCRIBE 헤더의 Authorization: {}", accessor.getFirstNativeHeader("Authorization"));
                SecurityCustomUser securityCustomUser = (SecurityCustomUser)authentication.getPrincipal();

                MemberVO memberVO = securityCustomUser.getMemberVO();

                String destination = accessor.getDestination();
                if (destination == null) {
                    throw new IllegalArgumentException("Destination 정보가 없습니다.");
                }

                String roomId = destination.split("/")[2];
                if (!chatService.isRoomParticipant(memberVO.getMemberEmail(), Long.parseLong(roomId))) {
                    throw new AuthenticationServiceException("해당 채팅방에 참여할 권한이 없습니다.");
                }
                log.info("SUBSCRIBE 권한 검증 완료: user={}, roomId={}", memberVO.getMemberEmail(), roomId);

            } catch (Exception e) {
                log.error("SUBSCRIBE 처리 중 예외 발생", e);
                throw e;
            }
        }

        return message;
    }
    
    // 쿼리 파라미터에서 토큰을 추출하는 헬퍼 메소드
    private String extractTokenFromQueryParam(StompHeaderAccessor accessor) {
        // NativeHeader는 MultiValueMap 형태이므로 List<String>으로 반환됩니다.
        java.util.List<String> tokenList = accessor.getNativeHeader("token");
        if (tokenList != null && !tokenList.isEmpty()) {
            // "Bearer " 접두사가 있다면 제거하고, 없다면 그대로 반환
            String bearerToken = tokenList.get(0);
            if (bearerToken.startsWith(BEARER_PREFIX)) {
                return bearerToken.substring(BEARER_PREFIX.length());
            }
            return bearerToken;
        }
        return null;
    }
}
