package udpm.hn.server.core.headsubject.plan.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface HSPLTutorClassDetailResponse extends IsIdentify, HasOrderNumber {

    String getTutorClassCode();
    String getTeacherTutor();
    String getStudentTutor();
    String getRoom();
    String getName();
    String getSubjectId();
    Long getStartTime();
    Long getEndTime();
    String getShift();

}
