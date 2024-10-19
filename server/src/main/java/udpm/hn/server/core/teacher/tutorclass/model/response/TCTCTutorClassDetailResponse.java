package udpm.hn.server.core.teacher.tutorclass.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface TCTCTutorClassDetailResponse extends IsIdentify, HasOrderNumber {

    String getClassCode();
    Long getStartTime();
    Long getEndTime();
    String getSubjectName();
    String getTotalStudent();
    String getStudent();
    String getShift();
    String getFormat();

}
