package kb_hack.backend.domain.notice.service;

import kb_hack.backend.domain.notice.dto.response.NoticeDTO;
import kb_hack.backend.domain.notice.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeMapper noticeMapper;

    public List<NoticeDTO> getAllnotice() {
        List<NoticeDTO> notices = noticeMapper.getAllNotice();
        return notices;
    }

}
