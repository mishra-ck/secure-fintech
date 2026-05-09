package secure.fintech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import secure.fintech.domain.entity.user.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    void recordSuccessfulLogin(UUID id, LocalDateTime now, String ip);
    Optional<User> findByEmail(String email);

    /*TODO*/
}
