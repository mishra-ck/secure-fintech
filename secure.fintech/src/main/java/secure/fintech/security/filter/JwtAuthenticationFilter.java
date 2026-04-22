package secure.fintech.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import secure.fintech.auth.jwt.JwtTokenProvider;

/**
 * JWT Authentication Filter - one per request
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;
}
