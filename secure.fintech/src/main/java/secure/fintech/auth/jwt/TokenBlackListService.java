package secure.fintech.auth.jwt;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlackListService {
    private final Map<String , Instant> blackList = new ConcurrentHashMap<>();
    // black listing a token for remaining TTL
    public void blackList(String jti, long ttlSeconds) {
        blackList.put(jti,Instant.now().plusSeconds(ttlSeconds));
    }
    /*TODO*/
}
