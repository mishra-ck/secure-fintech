package secure.fintech.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import secure.fintech.domain.dto.request.LoginRequest;
import secure.fintech.domain.dto.response.TokenResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    public TokenResponse login(LoginRequest request, String ip, String userAgent) {
        /*TODO*/
        return null;
    }

}
