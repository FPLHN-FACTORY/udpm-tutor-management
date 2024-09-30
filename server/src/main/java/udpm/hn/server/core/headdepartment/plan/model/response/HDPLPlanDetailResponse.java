package udpm.hn.server.core.headdepartment.plan.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface HDPLPlanDetailResponse extends IsIdentify, HasOrderNumber {

    String getPlanName();
    String getBlockName();
    String getBlockId();
    String getSemesterName();
    String getSemesterId();
    String getDescription();
    String getDepartmentName();
    String getFacilityName();
    Long getNumberSubjects();
    String getStatus();
    Long getStartTime();
    Long getEndTime();

}
