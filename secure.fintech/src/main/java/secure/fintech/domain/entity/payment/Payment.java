package secure.fintech.domain.entity.payment;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "idempotency_key", unique = true, length = 255)
    private String idempotencyKey;

    @Column(unique = true, nullable = false, length = 50)
    private String reference;

    // ─── Amount ──────────────────────────────────────────────────
    @Column(nullable = false, precision = 20, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    // ─── Parties ─────────────────────────────────────────────────
    @Column(name = "initiator_id", nullable = false)
    private UUID initiatorId;

    @Column(name = "beneficiary_name_enc", nullable = false, length = 500)
    private String beneficiaryNameEnc;

    @Column(name = "beneficiary_iban_enc", nullable = false, length = 500)
    private String beneficiaryIbanEnc;

    @Column(name = "beneficiary_iban_hash", nullable = false, length = 100)
    private String beneficiaryIbanHash;

    @Column(name = "beneficiary_bank_bic", length = 11)
    private String beneficiaryBankBic;

    @Column(name = "beneficiary_bank_name", length = 255)
    private String beneficiaryBankName;

    // ─── Payment Details ─────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false, length = 20)
    private PaymentType paymentType;

    @Column(name = "reference_message", length = 140)
    private String referenceMessage;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

}
