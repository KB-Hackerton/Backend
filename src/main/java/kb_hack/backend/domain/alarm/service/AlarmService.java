package kb_hack.backend.domain.alarm.service;

import kb_hack.backend.domain.alarm.AlarmPreference;
import kb_hack.backend.domain.alarm.Notification;
import kb_hack.backend.domain.alarm.dto.AlarmPreferenceReq;
import kb_hack.backend.domain.alarm.dto.AlarmPreferenceRes;
import kb_hack.backend.domain.alarm.dto.NotificationCreateReq;
import kb_hack.backend.domain.alarm.dto.NotificationRes;
import kb_hack.backend.domain.alarm.mapper.AlarmMapper;
import kb_hack.backend.domain.member.mapper.MemberMapper;
import kb_hack.backend.global.common.exception.type.CustomException;
import kb_hack.backend.global.common.exception.type.ServerErrorException;
import kb_hack.backend.global.common.response.bad.BadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static kb_hack.backend.global.common.exception.enums.BadStatusCode.*;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmMapper alarmMapper;
    private final MemberMapper memberMapper;
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
            alarmMapper.insertPreference(p);
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

    //관리자 모드에서 알림 생성
    public void create( NotificationCreateReq req) {
        Long memberId = memberMapper.getMemberIdByEmail(req.getTargetEmail());
        if(memberId ==null){
                throw new CustomException(USERID_NOT_FOUND_EXCEPTION);
        }

        AlarmPreference pref = alarmMapper.findById(memberId);

        if(req.getNotiType().equals("sos") && !Boolean.TRUE.equals(pref.getSosPreference())){
            throw new ServerErrorException(FAIL_TO_SEND_ALARM);
        }
        if(req.getNotiType().equals("announce") && !Boolean.TRUE.equals(pref.getAnnouncePreference())){
            throw new ServerErrorException(FAIL_TO_SEND_ALARM);
        }

        //잠자기 설정 했으면
        if (Boolean.TRUE.equals(pref.getIsAlarm())) {
            LocalTime now = LocalTime.now();
            LocalTime start = pref.getAlarmStartTime().toLocalTime();
            LocalTime end = pref.getAlarmEndTime().toLocalTime();

            boolean inDnd;

            if (start.isBefore(end)) {
                inDnd = now.isAfter(start) && now.isBefore(end);
            } else {
                // 자정을 넘기는 구간
                inDnd = now.isAfter(start) || now.isBefore(end);
            }
            if (inDnd) {
                throw new ServerErrorException(FAIL_TO_SEND_ALARM2);
            }
        }
        Notification p = new Notification();
        p.setMemberId(memberId);
        p.setTitle(req.getTitle());
        p.setContent(req.getContent());
        p.setNotiType(req.getNotiType());
        alarmMapper.insert(p);
    }

    public void createAll(NotificationCreateReq req) {
        List<Long> memberIds = memberMapper.getAllMemberId();

        for(Long memberId : memberIds){
            if(memberId ==null){
                throw new CustomException(USERID_NOT_FOUND_EXCEPTION);
            }
            AlarmPreference pref = alarmMapper.findById(memberId);
            if (pref == null) {
                continue; // 설정 없으면 그냥 스킵
            }

            LocalTime now = LocalTime.now();
            LocalTime start = pref.getAlarmStartTime().toLocalTime();
            LocalTime end = pref.getAlarmEndTime().toLocalTime();

            boolean inDnd;
            if (start.isBefore(end)) {
                // 자정을 넘지 않는 경우
                inDnd = now.isAfter(start) && now.isBefore(end);
            } else {
                // 자정을 넘는 경우
                inDnd = now.isAfter(start) || now.isBefore(end);
            }

            // 2. 방해 금지 시간이면 알림 생성 안 함
            if (inDnd) {
                continue;
            }
            Notification p = new Notification();
            p.setMemberId(memberId);
            p.setTitle(req.getTitle());
            p.setContent(req.getContent());
            p.setNotiType(req.getNotiType());
            alarmMapper.insert(p);
        }
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
