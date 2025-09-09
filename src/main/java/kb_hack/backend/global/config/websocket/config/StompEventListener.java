package kb_hack.backend.global.config.websocket.config;

// 스프링과 stomp는 기본적으로 새션관리를 자동(내부적)으로 처리
// 연결/해제 이벤트를 기록, 연결된 세 새션수를 실시가능로 확인할 목적으로 리스너를 생성 => 로그, 디버길 목적

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class StompEventListener {

	private final Set<String> sessions = ConcurrentHashMap.newKeySet();

	@EventListener
	public void connectHandle(SessionConnectedEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		sessions.add(accessor.getSessionId());
		System.out.println("connect session ID = " + accessor.getSessionId());
		System.out.println("total session : " + sessions.size());
	}

	@EventListener
	public void disconnectHandle(SessionDisconnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		sessions.remove(accessor.getSessionId());
		System.out.println("disconnect session ID = " + accessor.getSessionId());
		System.out.println("total session : " + sessions.size());
	}



}
