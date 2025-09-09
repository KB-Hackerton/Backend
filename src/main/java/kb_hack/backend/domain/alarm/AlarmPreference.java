package kb_hack.backend.domain.alarm;

import lombok.*;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmPreference {
        private Long alarmPreferenceId;
        private Long memberId;
        private Boolean sosPreference;
        private Boolean announcePreference;
        private Boolean isAlarm;               // DND 사용 여부
        private LocalDateTime alarmStartTime; // DB는 DATETIME (날짜는 의미 X)
        private LocalDateTime alarmEndTime;
}
