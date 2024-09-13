package udpm.hn.server.core.admin.semester.model.request;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;

@Getter
@Setter
public class SemesterRequest extends PageableRequest {

    private String semesterName;

    private Long semesterYear;

}
