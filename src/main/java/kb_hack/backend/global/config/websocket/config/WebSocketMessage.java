package kb_hack.backend.global.config.websocket.config;// WebSocketMessage.java (추천!)
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage<T> {
    private String type; // "CHAT", "READ_UPDATE" 등 메시지 종류
    private T payload;   // 실제 데이터 (ChatMessageResponse 또는 ReadUpdateInfo)
}
