package secure.fintech.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import secure.fintech.domain.entity.payment.PaymentStatus;
import secure.fintech.domain.entity.payment.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PaymentResponse {
    private UUID id;
    private String reference;
    private BigDecimal amount;
    private String currency;
    private String beneficiaryNameMasked;
    private String beneficiaryIbanMasked;
    private String beneficiaryBankBic;
    private PaymentType paymentType;
    private String referenceMessage;
    private PaymentStatus status;
    private LocalDate scheduledDate;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private String approvalNotes;
    private String failureReason;
}
