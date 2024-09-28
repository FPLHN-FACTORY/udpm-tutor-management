package udpm.hn.server.core.headdepartment.headplans.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface HeadPlanResponse extends IsIdentify, HasOrderNumber {

    String getPlanName();

    String getBlockName();

    String getDepartmentName();

    String getFacilityName();

    Long getNumberSubjects();

    String getStatus();

}
