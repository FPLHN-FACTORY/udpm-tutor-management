package udpm.hn.server.core.headdepartment.headsubjects.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReassignHeadSubjectRequest {

    @NotNull(message = "Current head subject id is required")
    private String currentHeadSubjectId;

    @NotNull(message = "New head subject id is required")
    private String newHeadSubjectId;

    @NotNull(message = "SemesterId is required")
    private String semesterId;

    @NotNull(message = "FacilityId is required")
    private String facilityId;
}
