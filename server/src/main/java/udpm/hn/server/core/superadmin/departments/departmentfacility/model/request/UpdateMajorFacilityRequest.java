package udpm.hn.server.core.superadmin.departments.departmentfacility.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMajorFacilityRequest {

    @NotNull
    private String majorId;

    @NotNull
    private String headMajorId;

}