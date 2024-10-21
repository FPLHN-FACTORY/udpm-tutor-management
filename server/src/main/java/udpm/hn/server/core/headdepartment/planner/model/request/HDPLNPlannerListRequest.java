package udpm.hn.server.core.headdepartment.planner.model.request;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;

@Getter
@Setter
public class HDPLNPlannerListRequest extends PageableRequest {

    private String currentSemesterId;

    private String currentBlockId;

    private String currentUserId;

    private String q;

    private String namePlanner;

    private String currentFacilityCode;

    private String headSubjectRoleCode;

    private String currentDepartmentCode;

}