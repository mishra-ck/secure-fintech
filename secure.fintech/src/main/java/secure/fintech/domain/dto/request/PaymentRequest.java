package secure.fintech.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import secure.fintech.domain.entity.payment.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentRequest {
    @NotNull @Positive
    private BigDecimal amount;
    @NotBlank @Size(min = 3, max = 30)
    private String currency;
    @NotBlank
    private String beneficiaryName;
    @NotBlank @Size(min = 15, max = 35)
    private String beneficiaryIBAN;
    @Size(min = 8,max = 11)
    private String beneficiaryBankBIC;
    private PaymentType paymentType;
    @Size(max = 120)
    private String referenceMessage;
    private LocalDate scheduledDate;
}
