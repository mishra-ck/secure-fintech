package secure.fintech.auth.jwt;

public record TokenPair(String accessToken, String refreshToken, long expiresIn) { }
