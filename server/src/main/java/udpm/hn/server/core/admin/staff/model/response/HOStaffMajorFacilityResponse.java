package udpm.hn.server.core.admin.staff.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface HOStaffMajorFacilityResponse extends IsIdentify, HasOrderNumber {

    String getFacilityName();

    String getDepartmentName();

    String getMajorName();

}
