package udpm.hn.server.infrastructure.constant;

public enum PlanStatus {
    PLANNING,
    PLANNER_APPROVED,
    HEAD_DEPARTMENT_APPROVED,
    IN_PROGRESS,
    DONE;

    public static PlanStatus fromString(String status) {
        for (PlanStatus planStatus : PlanStatus.values()) {
            if (planStatus.name().equalsIgnoreCase(status)) {
                return planStatus;
            }
        }
        return null;
    }

}
