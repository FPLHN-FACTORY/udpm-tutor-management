package udpm.hn.server.core.headsubject.plan.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CreatePlannerRequest {
    private String name;
    private String staffCode;
    private String emailFe;
    private String emailFpt;

    private String currentSemesterId;

    private String currentBlockId;

    private String currentUserId;

    private String namePlanner;

    private String currentFacilityCode;

    private String headSubjectRoleCode;

    private String currentDepartmentCode;

    public AssignedPlannerRequest toAssignedPlannerRequest() {
        AssignedPlannerRequest assignedPlannerRequest = new AssignedPlannerRequest();
        assignedPlannerRequest.setNamePlanner(this.namePlanner);
        assignedPlannerRequest.setHeadSubjectRoleCode(this.headSubjectRoleCode);
        assignedPlannerRequest.setCurrentBlockId(this.currentBlockId);
        assignedPlannerRequest.setCurrentUserId(this.currentUserId);
        assignedPlannerRequest.setCurrentSemesterId(this.currentSemesterId);
        assignedPlannerRequest.setCurrentDepartmentCode(this.currentDepartmentCode);
        assignedPlannerRequest.setCurrentFacilityCode(this.currentFacilityCode);
        return assignedPlannerRequest;
    }

}
