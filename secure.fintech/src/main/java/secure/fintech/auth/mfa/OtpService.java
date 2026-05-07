package secure.fintech.auth.mfa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import secure.fintech.encryption.EncryptionService;

@Service
@Slf4j
public class OtpService {
    public boolean verify(String mfaSecretEnc, String otpCode, EncryptionService encryptionService) {
        /*TODO*/
        return false;
    }

}
