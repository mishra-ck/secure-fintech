package secure.fintech.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import secure.fintech.auth.mfa.OtpService;
import secure.fintech.domain.dto.request.LoginRequest;
import secure.fintech.domain.dto.request.RefreshRequest;
import secure.fintech.domain.dto.response.MfaSetupResponse;
import secure.fintech.domain.dto.response.TokenResponse;
import secure.fintech.domain.entity.user.User;
import secure.fintech.encryption.EncryptionService;
import secure.fintech.security.CustomUserDetails;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final EncryptionService encryptionService;
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
            /*TODO*/

        }catch(AuthenticationException ex){

        }
        return null;
    }

    @Transactional
    private boolean checkAndUseBackupCode(User user, String inputCode) {
        /*TODO*/
        return false;
    }

    public TokenResponse refresh(RefreshRequest request) {
        /*TODO*/
        return null;
    }

    public void logout(String authHeader, String email) {
        /*TODO*/
    }

    public MfaSetupResponse setupMfa(String email) {
        /*TODO*/
        return  null;
    }
}
