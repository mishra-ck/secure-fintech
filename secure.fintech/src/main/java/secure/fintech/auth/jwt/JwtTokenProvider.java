package secure.fintech.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import secure.fintech.service.CustomUserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    private final TokenBlackListService tokenBlackListService;

    @Value("${app.jwt.access-expiry-seconds:900}")
    private long accessExpirySeconds;
    @Value("${app.jwt.refresh-expiry-seconds:86400}")
    private long refreshExpirySeconds;
    @Value("${app.jwt.stepup-expiry-seconds:300}")
    private long stepupExpirySeconds;
    @Value("${app.jwt.issuer:secure-fintech}")
    private String issuer;

    public JwtTokenProvider(@Value("${app.jwt.secret}") String secretKey,
                            TokenBlackListService tokenBlackListService) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.tokenBlackListService = tokenBlackListService;
    }

    // Token Generation
    public TokenPair generateTokenPair(Authentication auth, boolean mfaVerified){
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        Instant now = Instant.now();
        String jti = UUID.randomUUID().toString();

        String accessToken = Jwts.builder()
                .id(jti)
                .subject(user.getUsername())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessExpirySeconds)))
                .claim("uid", user.getUserId().toString())
                .claim("roles", extractRoles(auth))
                .claim("perms", extractPerms(auth))
                .claim("eid", user.getEntityId())
                .claim("mfa", mfaVerified)
                .claim("type", "access")
                .signWith(secretKey)
                .compact();

        String refreshJti = UUID.randomUUID().toString();
        String refreshToken = Jwts.builder()
                .id(refreshJti)
                .subject(user.getUsername())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(refreshExpirySeconds)))
                .claim("uid",user.getUserId().toString())
                .claim("type","refresh")
                .signWith(secretKey)
                .compact();

        return new TokenPair(accessToken,refreshToken,accessExpirySeconds);

    }
    //Handling step-up auth for MFA, for high value operation
    public String generateStepUpToken(String emailId,String userId, String operationType){
        Instant now = Instant.now();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(emailId)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(stepupExpirySeconds)))
                .claim("uid",userId)
                .claim("type","stepup")
                .claim("op",operationType)
                .claim("mfa",true)
                .signWith(secretKey)
                .compact();
    }
    // Token Validation
    public Claims validateAndExtractClaims(String token){
        try{
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .requireIssuer(issuer)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Check blacklisted
            if(tokenBlackListService.isBlackListed(claims.getId())){
                throw new JwtException("Token jas been revoked..!");
            }
            return claims;
        }catch (ExpiredJwtException e) {
            log.debug("JWT expired: {}", e.getMessage());
            throw e;
        } catch (SignatureException e) {
            log.warn("JWT signature invalid");
            throw e;
        }catch(JwtException e){
            log.warn("JWT Token Validation failed : {} ", e.getMessage());
            throw e;
        }
    }

    public void revokeToken(String jti, long ttlSeconds){
        tokenBlackListService.blackList(jti,ttlSeconds);
    }
    private List<String> extractRoles(Authentication auth){
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(x -> x.startsWith("ROLE_"))
                .map(y -> y.substring(5))
                .toList();
    }
    private List<String> extractPerms(Authentication auth){
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(x -> !x.startsWith("ROLE_"))
                .toList();
    }


}
