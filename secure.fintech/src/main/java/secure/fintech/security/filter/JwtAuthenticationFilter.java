package secure.fintech.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import secure.fintech.auth.jwt.JwtPrincipal;
import secure.fintech.auth.jwt.JwtTokenProvider;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

/**
 * JWT Authentication Filter - one per request
 *
 * Steps:
 * 1. Skip public endpoints
 * 2. Extract Bearer token from Authorization Header
 * 3. Validate JWT (signature, expiry, blacklist)
 * 4. Build Authentication from Claims (no DB calls - claims carry everything)
 * 5. Set into SecurityContextHolder for downstream use
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

                var authorities = Stream.concat(
                        roles != null ? roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_"+ r)):Stream.empty(),
                        perms != null ? perms.stream().map(SimpleGrantedAuthority::new):Stream.empty()
                ).toList();

                // Build principal record from JWT claims
                JwtPrincipal principal = new JwtPrincipal(
                        claims.getSubject(),
                        claims.get("uid",String.class),
                        claims.get("eid",String.class),
                        Boolean.TRUE.equals(claims.get("mfa", Boolean.class)),
                        claims.getId()
                );

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(principal, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }catch(JwtException je){
                log.debug("JWT Authentication failed for {} : {}", request.getRequestURI(), je.getMessage());
            }
        }
        filterChain.doFilter(request,response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(StringUtils.hasText(header) && header.startsWith("Bearer ")){
                return header.substring(7).trim();
        }
        return null;
    }

    private boolean isPublicPath(String uri){
        return PUBLIC_URLS.stream().anyMatch(uri::startsWith);
    }
}
