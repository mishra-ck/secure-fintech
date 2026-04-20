package secure.fintech.auth.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.map.KeySpaceStore;
import org.springframework.security.crypto.encrypt.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.KeyStore;

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
        this.secretKey = null;
        this.blackListService = blackListService;
    }

}
