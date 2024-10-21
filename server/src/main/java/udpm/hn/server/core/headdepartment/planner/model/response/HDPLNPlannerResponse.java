package udpm.hn.server.core.headdepartment.planner.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface HDPLNPlannerResponse extends IsIdentify, HasOrderNumber {

    String getStaffCode();

    String getStaffName();

    String getEmailFPT();

    String getEmailFE();

    Short getIsAssigned();

    Integer getAssignedCount();

}