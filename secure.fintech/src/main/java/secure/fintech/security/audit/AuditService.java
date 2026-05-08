package secure.fintech.security.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditService {

    @Async
    public void logMfaFailure(String email) {
        /*TODO*/
    }
    @Async
    public void logMfaSuccess(String email) {
        /*TODO*/
    }

    public void logLoginSuccess(String email, String ip, String userAgent) {
        /*TODO*/
    }

    public void logLoginFailure(String email, String ip, String message) {
    }

    @Async
    public void logLogout(String email, String id) {
        /*TODO*/
    }
}
