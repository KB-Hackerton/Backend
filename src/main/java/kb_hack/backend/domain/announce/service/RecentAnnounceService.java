package kb_hack.backend.domain.announce.service;

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

    private final RedisTemplate<String,Long> recentAnnounceRedis;

        public void addRecentAnnounce(Long announceId) {
            MemberVO vo = getMemberVO();
            String email = vo.getMemberEmail();
            String key = email + ":recent-announce";

            recentAnnounceRedis.opsForList().remove(key, 0, announceId);
            recentAnnounceRedis.opsForList().leftPush(key, announceId);
            recentAnnounceRedis.opsForList().trim(key, 0, 2);
        }

        public List<Long> getRecentAnnounceId() {
            MemberVO vo = getMemberVO();
            String email = vo.getMemberEmail();
            String key = email + ":recent-announce";
            return recentAnnounceRedis.opsForList().range(key, 0, -1);
        }


    private static MemberVO getMemberVO() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityCustomUser securityUser = (SecurityCustomUser) authentication.getPrincipal();
        MemberVO vo = securityUser.getMemberVO();
        return vo;
    }

}
