package udpm.hn.server.core.headsubject.plan.model.response;

public interface HSPLPlanInfoResponse {
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

