package kb_hack.backend.domain.notice.service;

import kb_hack.backend.domain.notice.dto.response.NoticeDetailDTO;
import kb_hack.backend.domain.notice.dto.response.NoticeDTO;
import kb_hack.backend.domain.notice.mapper.NoticeMapper;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.ServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeMapper noticeMapper;

    public List<NoticeDTO> getAllnotice() {
        List<NoticeDTO> notices = noticeMapper.getAllNotice();
        if (notices.isEmpty()) {
            throw new ServerErrorException(BadStatusCode.FAIL_TO_PROCESSING_NOTICE_EXCEPTION);
        }
        return notices;
    }

    public NoticeDetailDTO getNoticeById(Long noticeId) {
        NoticeDetailDTO detail = noticeMapper.getNoticeDetail(noticeId);
        return detail;
    }
}
