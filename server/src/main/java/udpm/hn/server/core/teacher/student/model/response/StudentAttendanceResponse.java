package udpm.hn.server.core.teacher.student.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface StudentAttendanceResponse extends IsIdentify, HasOrderNumber {

    String getRoom();
    String getStudentCode();
    String getStudentName();
    Boolean getIsAttendance();
    String getAttendanceTime();
    String getAttendanceReason();
    String getStudentId();
    String getNote();
}
