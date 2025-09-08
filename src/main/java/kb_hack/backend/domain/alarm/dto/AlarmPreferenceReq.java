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
public class AlarmPreferenceReq {
    @NotNull
    Boolean announceEnabled;
    @NotNull
    Boolean sosEnabled;
    @NotNull
    Boolean dndEnabled;
    @Pattern(regexp="^\\d{2}:\\d{2}$")
    String dndStart;
    @Pattern(regexp="^\\d{2}:\\d{2}$")
    String dndEnd;
}
