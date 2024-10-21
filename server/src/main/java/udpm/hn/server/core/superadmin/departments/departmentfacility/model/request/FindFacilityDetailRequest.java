package udpm.hn.server.core.superadmin.departments.departmentfacility.model.request;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;

@Getter
@Setter
public class FindFacilityDetailRequest extends PageableRequest {

    private String facilityName;

    private String staffCodeOrEmail;

}