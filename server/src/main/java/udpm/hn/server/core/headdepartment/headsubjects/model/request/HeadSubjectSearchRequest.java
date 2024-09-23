package udpm.hn.server.core.headdepartment.headsubjects.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class HeadSubjectSearchRequest {

    private String q;

    private String currentSemesterId;

    private String currentDepartmentCode;

    private String currentUserId;

    private String currentFacilityId;

    private String headSubjectId;

}
