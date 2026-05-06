package secure.fintech.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
@Data
public class MfaVerifyRequest {
    @NotBlank
    @Pattern(regexp = "[0-9\\s]{6,7}$",message = "OTP code must be 6 digits")
    private String otpCode;
}
