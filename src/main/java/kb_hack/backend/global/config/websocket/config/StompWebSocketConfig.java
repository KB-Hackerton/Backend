package kb_hack.backend.global.config.websocket.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import kb_hack.backend.global.util.JwtProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final JwtProcessor jwtProcessor;
	private final UserDetailsService userDetailsService;

	private final StompHandler stompHandler;


	// WebSocket 연결을 위한 엔드포인트 설정
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/connect")
			.setAllowedOrigins("http://localhost:5173","https://gyeongsang-app.vercel.app")
			.setHandshakeHandler(new CustomHandshakeHandler())

			// ws://가 아닌 http:// 엔드포인트 사용할 수 있게 해주는 sockJs 라이브러리를 통한 요청 허용하는 설정
			.withSockJS();

	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// publish/1형태로 메시지 발행 해야 함을 설정
		// /publish로 시작하는 url 패턴으로 메시지가 발행되면 @Controller 객체의 @MessageMapping 메서드로 라우팅
		registry.setApplicationDestinationPrefixes("/publish");

		// /topic/1형태로 메시지를 수신(subscribe) 해야 함을 설정
		registry.enableSimpleBroker("/topic");
	}


	// 웹 소켓 요청(connect, subscribe, publish)등의 요청시에는 http header등 http 메시지를 넣어올 수 있고,
	// 이를 interceptor를 통해 가로채서 토큰등을 검증 할 수 있음.
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompHandler);
	}


	// WebSocket 연결이 수립되기 직전, HTTP 핸드셰이크 단계에서 사용자를 인증하는 커스텀 핸들러
	class CustomHandshakeHandler extends DefaultHandshakeHandler {
		@Override
		protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
			// URL의 쿼리 스트링에서 토큰을 추출합니다.
			String queryString = request.getURI().getQuery();
			if (queryString != null && queryString.startsWith("token=")) {
				String token = queryString.substring(6);
				try {

					// 토큰 유효성을 검증하고 사용자 정보를 가져옵니다.
					jwtProcessor.validateToken(token);
					String username = jwtProcessor.getUsername(token);
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);

					// Spring Security의 Authentication 객체를 생성하여 Principal로 반환합니다.
					// 이 Principal 객체가 WebSocket 세션의 사용자가 됩니다.
					return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				} catch (Exception e) {
					log.error("핸드셰이크 중 토큰 기반 사용자 xác định 실패", e);
					return null; // 인증 실패 시 null 반환하여 연결 거부
				}
			}
			return null; // 토큰이 없는 경우 null 반환하여 연결 거부
		}
	}


}
