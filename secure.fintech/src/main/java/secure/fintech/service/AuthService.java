package secure.fintech.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import secure.fintech.domain.dto.request.LoginRequest;
import secure.fintech.domain.dto.request.RefreshRequest;
import secure.fintech.domain.dto.response.MfaSetupResponse;
import secure.fintech.domain.dto.response.TokenResponse;
import secure.fintech.domain.entity.user.User;
import secure.fintech.security.CustomUserDetails;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    @Transactional
    public TokenResponse login(LoginRequest request, String ip, String userAgent) {
        try {
            // Step : Authenticate Credentials
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            User user = userDetails.getUser();
            /*TODO*/

        }catch(AuthenticationException ex){

        }
        return null;
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
