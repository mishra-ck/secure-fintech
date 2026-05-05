package secure.fintech.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import secure.fintech.domain.dto.request.LoginRequest;
import secure.fintech.domain.dto.request.RefreshRequest;
import secure.fintech.domain.dto.response.MfaSetupResponse;
import secure.fintech.domain.dto.response.TokenResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    public TokenResponse login(LoginRequest request, String ip, String userAgent) {
        /*TODO*/
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
