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
import secure.fintech.domain.dto.request.RefreshRequest;
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


    /**
     * Login with email/password + optional OTP
     */
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
    /**
     * Refresh access token using valid refresh token
     * Revokes old refresh token and issues a new token pair.
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
            @RequestBody @Valid RefreshRequest request){
        return ResponseEntity.ok(authService.refresh(request));
    }

    private String extractIp(HttpServletRequest httpRequest) {
       String xIP = httpRequest.getHeader("X-Forwarded-For");
       return (xIP != null && !xIP.isBlank())
               ? xIP.split(",")[0].trim():httpRequest.getRemoteAddr();
    }
}
