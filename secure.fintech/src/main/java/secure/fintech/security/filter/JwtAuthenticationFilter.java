package secure.fintech.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import secure.fintech.auth.jwt.JwtTokenProvider;
import java.io.IOException;
import java.util.List;

/**
 * JWT Authentication Filter - one per request
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final List<String> PUBLIC_URLS = List.of(
            "/actuator/health","/actuator/info","/swagger-ui","/h2/console","/api-docs"
    );
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if(isPublicPath(request.getRequestURI())){
            filterChain.doFilter(request, response);
            return;
        }
        String token = extractToken(request);
        /*TODO*/
    }

    private String extractToken(HttpServletRequest request) {
        /*TODO*/
        return null;
    }

    private boolean isPublicPath(String uri){
        return PUBLIC_URLS.stream().anyMatch(uri::startsWith);
    }
}
