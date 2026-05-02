package secure.fintech.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
/**
 * Login request - standard email/password + optional OTP code
 */
@Data
public class LoginRequest {

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 8, max = 100)
    private String password;

    /** 6 digit otp code, required if MFA enabled */
    @Pattern(regexp = "^[0-9\\s]{6,7}",message = "OTP code must be of 6 digits")
    private String otpCode;
}
