package kb_hack.backend.domain.alarm.mapper;

import kb_hack.backend.domain.alarm.AlarmPreference;
import kb_hack.backend.domain.alarm.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmMapper {
    AlarmPreference findById(@Param("memberId") Long memberId);
    int updateSelective(@Param("p") AlarmPreference p);


    List<Notification> findByMemberId(@Param("memberId") Long memberId);
    int insert (@Param("p") Notification p);
    int markRead(@Param("memberId") Long memberId, @Param("notificationId") Long notificationId);
    int markAllRead(@Param("memberId") Long memberId);
    int deleteOne(@Param("memberId") Long memberId, @Param("notificationId") Long notificationId);

    int deleteAll(@Param("memberId") Long memberId);





}
