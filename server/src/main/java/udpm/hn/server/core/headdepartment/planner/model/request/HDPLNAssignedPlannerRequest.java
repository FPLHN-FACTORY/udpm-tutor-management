package udpm.hn.server.core.headdepartment.planner.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HDPLNAssignedPlannerRequest {

    private String currentSemesterId;

    private String currentBlockId;

    private String currentUserId;

    private String namePlanner;

    private String currentFacilityCode;

    private String headSubjectRoleCode;

    private String currentDepartmentCode;

}