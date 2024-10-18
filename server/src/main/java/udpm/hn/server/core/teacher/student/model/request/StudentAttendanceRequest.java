package udpm.hn.server.core.teacher.student.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentAttendanceRequest {
    private String studentId;
    private String lectureId;
    private Boolean isPresent;
    private String note;
}
