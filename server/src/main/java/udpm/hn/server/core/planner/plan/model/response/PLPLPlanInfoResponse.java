package udpm.hn.server.core.planner.plan.model.response;

public interface PLPLPlanInfoResponse {
    String getId();
    String getPlanName();
    String getBlockName();
    String getStatus();
    String getFacilityName();
    Long getNumberSubjects();
    Long getNumberClasses();
    Long getStartTime();
    Long getEndTime();

}

