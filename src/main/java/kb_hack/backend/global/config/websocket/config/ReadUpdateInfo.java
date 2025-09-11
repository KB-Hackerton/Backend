package kb_hack.backend.global.config.websocket.config;// ReadUpdateInfo.java
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadUpdateInfo {
    private Long roomId;
    private Long readerId;      // 누가 읽었는지
    private String readerEmail; // 누가 읽었는지 (이메일)
}
