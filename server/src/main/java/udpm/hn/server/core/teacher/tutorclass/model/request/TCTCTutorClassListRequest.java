package udpm.hn.server.core.teacher.tutorclass.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import udpm.hn.server.core.common.base.PageableRequest;

@Getter
@Setter
@ToString
public class TCTCTutorClassListRequest extends PageableRequest {

    private String classCode;

    private String subjectId;

    private String teacherId;

}
