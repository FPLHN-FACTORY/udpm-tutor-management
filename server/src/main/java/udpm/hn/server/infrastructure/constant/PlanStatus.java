package udpm.hn.server.infrastructure.constant;

public enum PlanStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELED,
    COMPLETED;

    public static PlanStatus fromString(String status) {
        for (PlanStatus planStatus : PlanStatus.values()) {
            if (planStatus.name().equalsIgnoreCase(status)) {
                return planStatus;
            }
        }
        return null;
    }

}
