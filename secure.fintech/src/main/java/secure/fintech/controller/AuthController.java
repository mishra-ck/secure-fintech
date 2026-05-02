package secure.fintech.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import secure.fintech.domain.dto.request.LoginRequest;
import secure.fintech.domain.dto.response.TokenResponse;
import secure.fintech.service.AuthService;

/**
 * Authentication Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody @Valid LoginRequest request, HttpServletRequest httpRequest ){

        String ip = extractIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        TokenResponse response = authService.login(request,ip,userAgent);

        if(response.isMfaRequired()){
            // 202 accepted - must submit otp code
            return ResponseEntity.accepted().body(response);
        }
        return ResponseEntity.ok(response);
    }

    private String extractIp(HttpServletRequest httpRequest) {
        /*TODO*/
        return null;
    }
}
