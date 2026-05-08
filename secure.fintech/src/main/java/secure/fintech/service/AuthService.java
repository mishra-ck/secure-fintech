package secure.fintech.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import secure.fintech.auth.jwt.JwtTokenProvider;
import secure.fintech.auth.jwt.TokenPair;
import secure.fintech.auth.mfa.OtpService;
import secure.fintech.domain.dto.request.LoginRequest;
import secure.fintech.domain.dto.request.RefreshRequest;
import secure.fintech.domain.dto.response.MfaSetupResponse;
import secure.fintech.domain.dto.response.TokenResponse;
import secure.fintech.domain.entity.user.User;
import secure.fintech.encryption.EncryptionService;
import secure.fintech.repository.UserRepository;
import secure.fintech.security.CustomUserDetails;
import secure.fintech.security.audit.AuditService;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final EncryptionService encryptionService;
    private final AuditService auditService;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    /** --------------------------- Login  ---------------------------- */
    @Transactional
    public TokenResponse login(LoginRequest request, String ip, String userAgent) {

        try {
            // Step 1: Authenticate Credentials
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            User user = userDetails.getUser();

            // Step 2 : MFA Check
            if(user.isMfaEnabled()){
                if(request.getOtpCode() == null || request.getOtpCode().isBlank()){
                    // inform client that MFA code is required, re-submit with otp code
                    return TokenResponse.builder()
                            .mfaRequired(true)
                            .tokenType("Bearer")
                            .build();
                }
            }
            boolean mfaValid = otpService.verify(
                    user.getMfaSecretEnc(), request.getOtpCode(),encryptionService);

            // check backup codes if OTP failed
            if(!mfaValid){
                mfaValid = checkAndUseBackupCode(user,request.getOtpCode());
            }
            if(!mfaValid){
                auditService.logMfaFailure(user.getEmail());
                throw new BadCredentialsException("Invalid MFA code");
            }
            auditService.logMfaSuccess(user.getEmail());

            // Step 3: Issue Tokens
            boolean mfaVerified = user.isMfaEnabled();
            TokenPair tokens = tokenProvider.generateTokenPair(auth, mfaVerified);

            // Step 4: update last login
            userRepository.recordSuccessfulLogin(user.getId(), LocalDateTime.now(), ip);
            auditService.logLoginSuccess(user.getEmail(), ip , userAgent);

            return TokenResponse.builder()
                    .accessToken(tokens.accessToken())
                    .refreshToken(tokens.refreshToken())
                    .expiresIn(tokens.expiresIn())
                    .mfaRequired(false)
                    .tokenType("Bearer")
                    .build();

        }catch(AuthenticationException ex){
            auditService.logLoginFailure(request.getEmail(), ip, ex.getMessage());
            throw ex;
        }
    }

    /**   ------------------- Refresh Token ---------------------   */
    public TokenResponse refresh(RefreshRequest request) {
        Claims claims = tokenProvider.validateAndExtractClaims(request.getRefreshToken());
        if(!"refresh".equals(claims.get("type",String.class))){
            throw new JwtException("Not a refresh token..!");
        }

        // Load fresh user state ( since permission may have changed since last token)
        CustomUserDetails userDetails =
                (CustomUserDetails) userDetailsService.loadUserByUsername(claims.getSubject());

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,null,userDetails.getAuthorities());

        // Revoke old refresh tokens
        tokenProvider.revokeToken(claims.getId(), 86400);

        boolean mfaVerified = Boolean.TRUE.equals(claims.get("mfa", Boolean.class));
        TokenPair tokenPair = tokenProvider.generateTokenPair(auth,mfaVerified);

        return TokenResponse.builder()
                .accessToken(tokenPair.accessToken())
                .accessToken(tokenPair.refreshToken())
                .expiresIn(tokenPair.expiresIn())
                .tokenType("Bearer")
                .build();
    }

    /** ----------------------------- Logout ---------------------------- */
    public void logout(String bearerToken, String email) {
        String token = bearerToken.startsWith("Bearer ") ? bearerToken.substring(7):bearerToken;
        try{
            Claims claims = tokenProvider.validateAndExtractClaims(token);
            tokenProvider.revokeToken(claims.getId(), 900);  // blacklist for access token TTL
            auditService.logLogout(email, claims.getId());
        }catch (JwtException je){
            log.debug("Logout with valid token for user {}", email);
        }
    }

    public MfaSetupResponse setupMfa(String email) {
        /*TODO*/
        return  null;
    }

    @Transactional
    private boolean checkAndUseBackupCode(User user, String inputCode) {
        /*TODO*/
        return false;
    }

}
