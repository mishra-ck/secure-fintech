package secure.fintech.auth.jwt;

public record JwtPrincipal(
        String email,
        String userId,
        String entityId,
        boolean mfaVerified,
        String jti
) {
    @Override
    public String toString(){ return email; }
}
