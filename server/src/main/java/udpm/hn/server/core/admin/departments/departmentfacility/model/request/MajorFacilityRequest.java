package udpm.hn.server.core.admin.departments.departmentfacility.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;

@Getter
@Setter
public class MajorFacilityRequest extends PageableRequest {

    private String headMajorName;

    private String majorName;

    private String headMajorCode;

    @NotNull
    private String departmentFacilityId;

}