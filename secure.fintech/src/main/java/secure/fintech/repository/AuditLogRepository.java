package secure.fintech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import secure.fintech.domain.entity.audit.AuditLog;

import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
/*TODO*/
}
