package udpm.hn.server.core.superadmin.departments.departmentfacility.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;

public interface DepartmentFacilityResponse extends HasOrderNumber {

    String getDepartmentFacilityId();

    String getFacilityId();

    String getFacilityCode();

    String getHeadOfDepartmentId();

    String getFacilityName();

    String getHeadOfDepartmentName();

    String getHeadOfDepartmentCode();

    Long getDepartmentFacilityStatus();

    Long getCreatedDate();

    String getProfileStaff();

}