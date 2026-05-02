package secure.fintech.domain.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private boolean mfaRequired;
    @Builder.Default
    private String tokenType = "Bearer";
}
