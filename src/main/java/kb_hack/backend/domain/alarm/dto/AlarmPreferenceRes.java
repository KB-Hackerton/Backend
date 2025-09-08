package kb_hack.backend.domain.alarm.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmPreferenceRes {
    Boolean announceEnabled;
    Boolean sosEnabled;
    Boolean dndEnabled;
    String dndStart;
    String dndEnd;
}
