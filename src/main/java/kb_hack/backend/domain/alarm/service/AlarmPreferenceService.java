package kb_hack.backend.domain.alarm.service;

import kb_hack.backend.domain.alarm.AlarmPreference;
import kb_hack.backend.domain.alarm.dto.AlarmPreferenceReq;
import kb_hack.backend.domain.alarm.dto.AlarmPreferenceRes;
import kb_hack.backend.domain.alarm.mapper.AlarmMapper;
import kb_hack.backend.domain.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AlarmPreferenceService {
    private final AlarmMapper alarmMapper;
    private static final LocalTime DEF_START_TIME = LocalTime.of(21, 0);
    private static final LocalTime DEF_END_TIME = LocalTime.of(8, 0);

    public AlarmPreferenceRes getOrCreate(Long memberId) {
        var p = alarmMapper.findById(memberId);
        if (p == null) {
            p = new AlarmPreference();
            p.setMemberId(memberId);
            p.setAnnouncePreference(true);
            p.setSosPreference(true);
            p.setIsAlarm(true);
            p.setAlarmStartTime(LocalDateTime.of(1970,1,1, DEF_START_TIME.getHour(), DEF_START_TIME.getMinute()));
            p.setAlarmEndTime(LocalDateTime.of(1970,1,1, DEF_END_TIME.getHour(), DEF_END_TIME.getMinute()));
            alarmMapper.upsert(p);
        }
        return toRes(p);
    }

    @Transactional
    public void save(Long memberId, AlarmPreferenceReq req) {
        var start = LocalTime.parse(req.getDndStart());
        var end   = LocalTime.parse(req.getDndEnd());

        var p = new AlarmPreference();
        p.setMemberId(memberId);
        p.setAnnouncePreference(req.getAnnounceEnabled());
        p.setSosPreference(req.getSosEnabled());
        p.setIsAlarm(req.getDndEnabled());
        p.setAlarmStartTime(LocalDateTime.of(1970,1,1, start.getHour(), start.getMinute()));
        p.setAlarmEndTime(LocalDateTime.of(1970,1,1, end.getHour(), end.getMinute()));
        alarmMapper.upsert(p);
    }

    private AlarmPreferenceRes toRes(AlarmPreference p) {
        LocalTime s = p.getAlarmStartTime() != null ? p.getAlarmStartTime().toLocalTime() : DEF_START_TIME;
        LocalTime e = p.getAlarmEndTime()   != null ? p.getAlarmEndTime().toLocalTime()   : DEF_END_TIME;

        return AlarmPreferenceRes.builder()
                .announceEnabled(Boolean.TRUE.equals(p.getAnnouncePreference()))
                .sosEnabled(Boolean.TRUE.equals(p.getSosPreference()))
                .dndEnabled(Boolean.TRUE.equals(p.getIsAlarm()))
                .dndStart(s.toString()) // "HH:mm"
                .dndEnd(e.toString())
                .build();
    }
}
