package kb_hack.backend.domain.announce.service;

import jakarta.annotation.PostConstruct;
import kb_hack.backend.domain.announce.Announce;
import kb_hack.backend.domain.announce.dto.AnnounceRankingDTO;
import kb_hack.backend.domain.announce.mapper.AnnounceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AnnounceRankingService {

    private final AnnounceMapper announceMapper;
    private final StringRedisTemplate redisTemplate;
    private static final String RANKING_KEY = "announce:ranking";

    @PostConstruct
    public void preloadRanking() {
        redisTemplate.delete(RANKING_KEY);
        List<AnnounceRankingDTO> all = announceMapper.findTopAnnounces(1000);
        for (AnnounceRankingDTO a : all) {
            redisTemplate.opsForZSet().add(RANKING_KEY, a.getAnnounceId().toString(), a.getViewNum() );
        }
    }

    public List<AnnounceRankingDTO> getTopN(int n) {
        // 1. Redis에서 가져오기
        Set<String> topIds = redisTemplate.opsForZSet().reverseRange(RANKING_KEY, 0, n - 1);

        // 2. Redis 비어 있으면 → DB에서 조회 후 Redis에 채움
        if (topIds == null || topIds.isEmpty()) {
            List<AnnounceRankingDTO> topFromDb = announceMapper.findTopAnnounces(n);

            for (AnnounceRankingDTO a : topFromDb) {
                redisTemplate.opsForZSet().add(RANKING_KEY, a.getAnnounceId().toString(), a.getViewNum());
            }
            return topFromDb;
        }

        // 3. Redis에 있으면 ID 기준으로 DB 조회
        List<AnnounceRankingDTO> results = new ArrayList<>();
        for (String idStr : topIds) {
            AnnounceRankingDTO a = announceMapper.findByAnnounceId(Long.valueOf(idStr));
            if (a != null) results.add(a);
        }
        return results;
    }


    public void increaseView(Long announceId) {
        // DB 업데이트
        announceMapper.increaseViewNum(announceId);

        // Redis 업데이트
        redisTemplate.opsForZSet().incrementScore(RANKING_KEY, announceId.toString(), 1);
    }
}

