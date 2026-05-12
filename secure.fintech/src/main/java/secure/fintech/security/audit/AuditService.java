package secure.fintech.security.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import secure.fintech.domain.entity.audit.AuditLog;
import secure.fintech.repository.AuditLogRepository;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditService {
    private final static Logger LOG = LoggerFactory.getLogger(AuditService.class);
    private final AuditLogRepository auditLogRepository;
    private static final String AUDIT_ENC_KEY = "audit-integrity-key-32-bytes-long";

    @Async
    public void logMfaSuccess(String email) {
        save(AuditLog.builder()
                .eventId(UUID.randomUUID())
                .timestamp(Instant.now())
                .userId(email)
                .eventType("MFA_VERIFY_SUCCESS")
                .outcome("SUCCESS")
                .severity("INFO")
                .regulationTags("NONE")
                .build()
        );
    }
    @Async
    public void logMfaFailure(String email) {
        save(AuditLog.builder()
                .eventId(UUID.randomUUID())
                .timestamp(Instant.now())
                .userId(email)
                .eventType("MFA_VERIFY_FAILURE")
                .outcome("FAILURE")
                .severity("WARNING")
                .regulationTags("NONE")
                .build()
        );
    }

    public void logLoginSuccess(String email, String ip, String userAgent) {
        save(AuditLog.builder()
                .eventId(UUID.randomUUID())
                .timestamp(Instant.now())
                .userId(email)
                .ipAddress(ip)
                .userAgent(userAgent) // truncate logic to be added
                .eventType("AUTH_LOGIN_SUCCESS")
                .outcome("SUCCESS")
                .severity("INFO")
                .regulationTags("NONE")  /*TODO*/
                .build()
        );
    }

    public void logLoginFailure(String email, String ip, String reason) {
        save(AuditLog.builder()
                .eventId(UUID.randomUUID())
                .timestamp(Instant.now())
                .userId(email)
                .ipAddress(ip)
                .eventType("AUTH_LOGIN_FAILURE")
                .outcome("FAILURE")
                .errorMessage(reason)
                .severity("WARNING")
                .regulationTags("NONE")
                .build());
    }

    @Async
    public void logLogout(String email, String jti) {
        save(AuditLog.builder()
                .eventId(UUID.randomUUID())
                .timestamp(Instant.now())
                .userId(email)
                .jwtId(jti)
                .eventType("AUTH_LOGOUT")
                .outcome("SUCCESS")
                .severity("INFO")
                .build());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(AuditLog.AuditLogBuilder builder){
        AuditLog log = builder
                .integrityHash(null)  /*TODO*/
                .build();
        try{
            auditLogRepository.save(log);
        }catch(Exception e){
            LOG.info("Audit log failed due to an exception : {}",e);
        }
    }
}
