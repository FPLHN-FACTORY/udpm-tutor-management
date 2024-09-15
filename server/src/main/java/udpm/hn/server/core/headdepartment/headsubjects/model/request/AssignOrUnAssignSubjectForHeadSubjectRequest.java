package udpm.hn.server.core.headdepartment.headsubjects.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignOrUnAssignSubjectForHeadSubjectRequest {

    @NotNull(message = "SubjectId is required")
    private String subjectId;

    @NotNull(message = "SemesterId is required")
    private String semesterId;

    @NotNull(message = "FacilityId is required")
    private String facilityId;

}
