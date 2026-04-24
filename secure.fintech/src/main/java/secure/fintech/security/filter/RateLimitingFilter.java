package secure.fintech.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate Limiting Filter - Token Bucket Algorithm
 */
@Component
@Slf4j
public class RateLimitingFilter extends OncePerRequestFilter {
    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    private static final Map<String,int[]> LIMITS = Map.of();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String clientKey = resolveClientKey(request);
        String uri = request.getRequestURI();
        int[] limit = resolveLimit(uri);
        /*TODO*/

    }
    private int[] resolveLimit(String uri) { return null;}
    private String resolveClientKey(HttpServletRequest request){/*TODO*/ return null;}
}
