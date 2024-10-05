package udpm.hn.server.core.planner.plan.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface PLPLPlanListResponse extends IsIdentify, HasOrderNumber {

    String getPlanName();
    String getBlockName();
    String getDepartmentName();
    String getFacilityName();
    Long getNumberSubjects();
    String getStatus();
    Long getStartTime();
    Long getEndTime();

}
