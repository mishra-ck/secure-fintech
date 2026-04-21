package secure.fintech.domain.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users",
    indexes = {
        @Index(columnList = "email", unique = true)
    }
)
@Getter  @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,unique = true,length = 255)
    private String email;

    @Column(nullable = false,length = 500)
    private String passwordHash;

    // Account State
    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean accountLocked = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean accountExpired = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean credentialExpired = false;

    // Lockout Policy
    @Builder.Default
    @Column(nullable = false)
    private int failedLoginAttempts = 0;

    private LocalDateTime lockoutUntil;

    // MFA
    @Builder.Default
    @Column(nullable = false)
    private boolean mfaEnabled = false;

    // Encrypted secret
    @Column(name = "mfa_secret_enc", length = 500)
    private String mfaSecretEnc;

    // Encrypted JSON array of backup codes
    @Column(name = "mfa_backup_codes_enc", length = 2000)
    private String mfaBackupCodesEnc;

    //  Password Policy
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime passwordChangedAt = LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime passwordExpiresAt = LocalDateTime.now().plusDays(90);

    // Profile
    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "entity_id", length = 50)
    private String entityId;

    @Column(length = 100)
    private String department;

    @Builder.Default
    @Column(nullable = false, precision = 20, scale = 4)
    private BigDecimal maxPaymentLimit = new BigDecimal("10000.00");

    @Builder.Default
    @Column(nullable = false, precision = 20, scale = 4)
    private BigDecimal maxDailyLimit = new BigDecimal("50000.00");

    // Roles
    @ManyToMany(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    // Audit
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    private LocalDateTime lastLoginAt;

    @Column(length = 45)
    private String lastLoginIp;


}
