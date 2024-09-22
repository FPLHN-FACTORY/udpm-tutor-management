package udpm.hn.server.core.headdepartment.headsubjects.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import udpm.hn.server.core.common.base.PageableRequest;

@Setter
@Getter
@ToString
public class SubjectByHeadSubjectRequest extends PageableRequest {

    private String currentSemesterId;

    private String departmentCode;

    private String facilityCode;

    private String headSubjectId;

}
