package udpm.hn.server.core.admin.departments.departmentfacility.model.response;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableObject;

@Getter
@Setter
public class MajorFacilitiesResponse {

    PageableObject<?> majorFacilities;

    FacilityDepartmentInfoResponse facilityDepartmentInfo;

}