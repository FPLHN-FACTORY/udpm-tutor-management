package udpm.hn.server.core.headdepartment.plan.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface HDPLPlanListResponse extends IsIdentify, HasOrderNumber {

    String getPlanName();
    String getBlockName();
    String getDepartmentName();
    String getFacilityName();
    Long getNumberSubjects();
    String getStatus();
    Long getStartTime();
    Long getEndTime();

}
