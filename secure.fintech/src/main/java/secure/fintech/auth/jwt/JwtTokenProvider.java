package secure.fintech.auth.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final TokenBlackListService blackListService;


    public JwtTokenProvider(SecretKey secretKey, TokenBlackListService blackListService) {
        this.secretKey = secretKey;
        this.blackListService = blackListService;
    }

}
