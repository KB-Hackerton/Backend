package kb_hack.backend.domain.alarm.service;

import kb_hack.backend.domain.alarm.AlarmPreference;
import kb_hack.backend.domain.alarm.Notification;
import kb_hack.backend.domain.alarm.dto.AlarmPreferenceReq;
import kb_hack.backend.domain.alarm.dto.AlarmPreferenceRes;
import kb_hack.backend.domain.alarm.dto.NotificationRes;
import kb_hack.backend.domain.alarm.mapper.AlarmMapper;
import kb_hack.backend.domain.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
            alarmMapper.updateSelective(p);
        }
        return toRes(p);
    }

    @Transactional
    public void partialUpdate(Long memberId, AlarmPreferenceReq req) {
        AlarmPreference p = new AlarmPreference();
        p.setMemberId(memberId);

        if (req.getAnnounceEnabled() != null)
            p.setAnnouncePreference(req.getAnnounceEnabled());

        if (req.getSosEnabled() != null)
            p.setSosPreference(req.getSosEnabled());

        if (req.getDndEnabled() != null)
            p.setIsAlarm(req.getDndEnabled());

        if (req.getDndStart() != null) {
            LocalTime t = LocalTime.parse(req.getDndStart());
            p.setAlarmStartTime(LocalDateTime.of(1970,1,1,t.getHour(),t.getMinute()));
        }

        if (req.getDndEnd() != null) {
            LocalTime t = LocalTime.parse(req.getDndEnd());
            p.setAlarmEndTime(LocalDateTime.of(1970,1,1,t.getHour(),t.getMinute()));
        }

        alarmMapper.updateSelective(p);
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

    private NotificationRes toRes(Notification n) {
        return NotificationRes.builder()
                .notificationId(n.getNotificationId())
                .title(n.getTitle())
                .content(n.getContent())
                .notiType(n.getNotiType())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }

    public List<NotificationRes> list (Long memberId) {
        return alarmMapper.findByMemberId(memberId).stream()
                .map(this::toRes)
                .toList();
    }

    public void create(Notification n) {
        alarmMapper.insert(n);
    }

    public void markRead(Long memberId, Long notificationId) {
        alarmMapper.markRead(memberId, notificationId);
    }

    public void markAllRead(Long memberId) {
        alarmMapper.markAllRead(memberId);
    }

    public void deleteOne(Long memberId, Long notificationId) {
        alarmMapper.deleteOne(memberId, notificationId);
    }

    public void deleteAll(Long memberId) {
        alarmMapper.deleteAll(memberId);
    }
}
