package udpm.hn.server.core.headdepartment.headsubjects.model.request;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;

@Setter
@Getter
public class HeadSubjectRequest extends PageableRequest {

    private String currentSemesterId;

    private String currentUserId;

    private String q;

    private String currentFacilityCode;

    private String headSubjectRoleCode;

    private String currentDepartmentCode;

}
