package kb_hack.backend.domain.alarm.mapper;

import kb_hack.backend.domain.alarm.AlarmPreference;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AlarmMapper {
    AlarmPreference findById(@Param("memberId") Long memberId);
    int upsert(@Param("p") AlarmPreference p);

}
