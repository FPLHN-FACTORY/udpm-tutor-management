package udpm.hn.server.core.headdepartment.plan.model.response;

public interface HDPLPlanInfoResponse {
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

