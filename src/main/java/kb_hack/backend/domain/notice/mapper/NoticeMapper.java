package kb_hack.backend.domain.notice.mapper;

import kb_hack.backend.domain.notice.dto.response.NoticeDTO;
import kb_hack.backend.domain.notice.dto.response.NoticeDetailDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
    List<NoticeDTO> getAllNotice();
    NoticeDetailDTO getNoticeDetail(Long noticeId);
}
