package secure.fintech.domain.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MfaSetupResponse {
    private String otpAuthUrl;
    private String[] backupCodes;
    private String message;
}