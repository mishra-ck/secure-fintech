package secure.fintech.domain.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String error;
    private String message;
    private String path;
    private int status;
    private String timeStamp;

}
