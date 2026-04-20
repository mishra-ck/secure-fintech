package secure.fintech.auth.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.map.KeySpaceStore;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.encrypt.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;
import secure.fintech.service.CustomUserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

/**
 * JWT Token Generator
 * Token Types:
 *  ACCESS  - 15 min : API calls
 *  REFRESH - 24 hrs : obtain new access token
 *  STEPUP  - 5 min  : MFA step-up for high value operations
 */
@Component
@Slf4j
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final TokenBlackListService blackListService;

    @Value("${app.jwt.access-expiry-seconds:900}")
    private long accessExpirySeconds;
    @Value("${app.jwt.refresh-expiry-seconds:86400}")
    private long refreshExpirySeconds;
    @Value("${app.jwt.stepup-expiry-seconds:300}")
    private long stepupExpirySeconds;
    @Value("${app.jwt.issuer:secure-fintech}")
    private String issuer;

    public JwtTokenProvider(@Value("${app.jwt.secret}") String secretKey,
                            TokenBlackListService blackListService) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.blackListService = blackListService;
    }

    // Token Generation
    public TokenPair generateTokenPair(Authentication auth, boolean mfaVerified){
        CustomUserDetails userDetails = null;
        /*TODO*/
        return null;
    }


}
