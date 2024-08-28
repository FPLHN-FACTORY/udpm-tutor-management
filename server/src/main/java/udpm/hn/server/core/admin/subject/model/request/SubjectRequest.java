package udpm.hn.server.core.admin.subject.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRequest extends PageableRequest {

    private String subjectCode;

    private String subjectName;

    private String departmentId;

    private String subjectType;

    private Long startDate;

}
