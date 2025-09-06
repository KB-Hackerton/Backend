package kb_hack.backend.domain.announce.service;

import kb_hack.backend.domain.announce.dto.RecentViewAnnounceDTO;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.ServerErrorException;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.entity.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentAnnounceService {

    private final StringRedisTemplate stringRedisTemplate;

    public void addRecentAnnounce(String announceId, String announceTitle) {
        try {
            MemberVO vo = getMemberVO();

            String email = vo.getMemberEmail();
            String key = email + ":recent-announce";
            String value = announceId + "|" + announceTitle;


            stringRedisTemplate.opsForList().remove(key, 0, value);
            stringRedisTemplate.opsForList().leftPush(key, value);
            stringRedisTemplate.opsForList().trim(key, 0, 2);

        } catch (Exception e) {
            throw new ServerErrorException(BadStatusCode.FAIL_TO_SAVE_RECENT_ANNOUNCE_EXCEPTION);
        }
    }
    public List<RecentViewAnnounceDTO> getRecentAnnounceList() {
        MemberVO vo = getMemberVO();

        String email = vo.getMemberEmail();
        String key = email + ":recent-announce";

        List<String> values = stringRedisTemplate.opsForList().range(key, 0, -1);

        if (values == null || values.isEmpty()) {
            throw new ServerErrorException(BadStatusCode.REDIS_FAIL_TO_GET_RECENT_ANNOUNCE_EXCEPTION);
        }

        try {
            return values.stream()
                    .map(v -> {
                        String[] parts = v.split("\\|", 2);
                        if (parts.length < 2) {
                            throw new ServerErrorException(BadStatusCode.REDIS_INVALID_DATA_FORMAT_EXCEPTION);
                        }
                        return new RecentViewAnnounceDTO(Long.valueOf(parts[0]), parts[1]);
                    })
                    .toList();
        } catch (NumberFormatException e) {
            throw new ServerErrorException(BadStatusCode.REDIS_INVALID_DATA_FORMAT_EXCEPTION);
        }
    }



    private static MemberVO getMemberVO() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityCustomUser securityUser = (SecurityCustomUser) authentication.getPrincipal();
        MemberVO vo = securityUser.getMemberVO();
        return vo;
    }

}
