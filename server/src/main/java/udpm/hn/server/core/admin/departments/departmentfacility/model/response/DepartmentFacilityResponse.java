package udpm.hn.server.core.admin.departments.departmentfacility.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;

public interface DepartmentFacilityResponse extends HasOrderNumber {

    String getDepartmentFacilityId();

    String getFacilityId();

    String getHeadOfDepartmentId();

    String getFacilityName();

    String getHeadOfDepartmentName();

    String getHeadOfDepartmentCode();

    Long getDepartmentFacilityStatus();

    Long getCreatedDate();

}