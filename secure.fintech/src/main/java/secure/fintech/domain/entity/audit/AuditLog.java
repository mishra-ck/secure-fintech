package secure.fintech.domain.entity.audit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.cert.CertPathBuilder;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog{

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(name = "event_id", nullable = false, unique = true, updatable = false)
        private UUID eventId;

        @Column(nullable = false, updatable = false)
        private Instant timestamp;

        // --- WHO ----
        @Column(name = "user_id", length = 255, updatable = false)
        private String userId;

        @Column(name = "ip_address", length = 45, updatable = false)
        private String ipAddress;

        @Column(name = "user_agent", length = 500, updatable = false)
        private String userAgent;

        @Column(name = "jwt_id", length = 36, updatable = false)
        private String jwtId;

        // --- WHAT ---
        @Column(name = "event_type", nullable = false, length = 100, updatable = false)
        private String eventType;

        @Column(length = 100, updatable = false)
        private String operation;

        @Column(name = "resource_type", length = 50, updatable = false)
        private String resourceType;

        @Column(name = "resource_id", updatable = false)
        private UUID resourceId;

        // --- RESULT ---
        @Column(nullable = false, length = 20, updatable = false)
        private String outcome;                   // SUCCESS, FAILURE, DENIED

        @Column(name = "http_status", updatable = false)
        private Integer httpStatus;

        @Column(name = "duration_ms", updatable = false)
        private Integer durationMs;

        // ---- DETAIL  ----
        @Column(name = "error_code", length = 50, updatable = false)
        private String errorCode;

        @Column(name = "error_message", length = 2000, updatable = false)
        private String errorMessage;

        @Column(length = 10, updatable = false)
        @Builder.Default
        private String severity = "INFO";         // INFO, WARNING, CRITICAL

        // Sensitive fields masked before storage (e.g., card numbers → ****1234)
        @Column(name = "request_summary", length = 2000, updatable = false)
        private String requestSummary;

        // ---  INTEGRITY  -----
        @Column(name = "integrity_hash", nullable = false, length = 100, updatable = false)
        private String integrityHash;

        // Compliance regulation tags
        @Column(name = "regulation_tags", length = 200, updatable = false)
        private String regulationTags;

}
