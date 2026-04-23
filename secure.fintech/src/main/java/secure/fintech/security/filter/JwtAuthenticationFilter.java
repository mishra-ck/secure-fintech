package secure.fintech.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final JwtTokenProvider tokenProvider;
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
        if(token != null && SecurityContextHolder.getContext().getAuthentication() == null){
            try{
                Claims claims = tokenProvider.validateAndExtractClaims(token);

                String type = claims.get("type", String.class);
                if(!"access".equals(type)){
                    log.warn("Non-access token used for API calls : type:{}",type);
                    filterChain.doFilter(request,response);
                    return;
                }
                // Build authorities from JWT claims
                List<String> roles = claims.get("roles",List.class);
                List<String> perms = claims.get("perms",List.class);
                /*TODO*/

            }catch(JwtException je){
                log.debug("JWT Authentication failed for {} : {}", request.getRequestURI(), je.getMessage());
            }
        }
    }

    private String extractToken(HttpServletRequest request) {
        /*TODO*/
        return null;
    }

    private boolean isPublicPath(String uri){
        return PUBLIC_URLS.stream().anyMatch(uri::startsWith);
    }
}
