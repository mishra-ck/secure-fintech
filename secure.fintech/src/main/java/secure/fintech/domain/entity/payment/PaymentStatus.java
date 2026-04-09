package secure.fintech.domain.entity.payment;

public enum PaymentStatus {
    PENDING,
    PENDING_APPROVAL,
    APPROVED,
    PROCESSING,
    COMPLETED,
    FAILED,
    CANCELLED
}
