package udpm.hn.server.core.headdepartment.plan.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface HDPLTutorClassDetailResponse extends IsIdentify, HasOrderNumber {

    String getTutorClassCode();
    String getTeacherTutor();
    String getStudentTutor();
    String getRoom();
    String getShift();
    Long getStartTime();
    Long getEndTime();

}
