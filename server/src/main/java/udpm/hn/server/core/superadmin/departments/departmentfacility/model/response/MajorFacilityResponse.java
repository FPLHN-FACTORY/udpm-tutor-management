package udpm.hn.server.core.superadmin.departments.departmentfacility.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface MajorFacilityResponse extends IsIdentify, HasOrderNumber {

    String getMajorName();

    String getHeadMajorCodeName();

    FacilityDepartmentInfoResponse getFacilityDepartmentInfo();

    String getStatus();

}