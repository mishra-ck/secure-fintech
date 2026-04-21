package secure.fintech.domain.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Permission naming convention : {RESOURCE}_{ACTION}
 * Ex: "TRADE_EXECUTE"
 */
@Entity
@Table(name = "permissions")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 50)
    private String resource;

    @Column(length = 50)
    private String action;

    @Column(length = 50)
    private String description;
}
