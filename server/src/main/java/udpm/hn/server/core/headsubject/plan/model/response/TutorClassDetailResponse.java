package udpm.hn.server.core.headsubject.plan.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface TutorClassDetailResponse extends IsIdentify, HasOrderNumber {

    String getTutorClassCode();
    String getTeacherTutor();
    String getStudentTutor();
    Long getStartTime();
    Long getEndTime();

}
