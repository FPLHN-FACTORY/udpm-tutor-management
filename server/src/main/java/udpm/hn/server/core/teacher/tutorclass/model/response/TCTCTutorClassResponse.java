package udpm.hn.server.core.teacher.tutorclass.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface TCTCTutorClassResponse extends IsIdentify, HasOrderNumber {

    String getClassCode();
    String getSubjectName();
    String getTotalStudent();
    String getStudentName();
    Long getStartTime();
    Long getEndTime();
    String getShift();

}
