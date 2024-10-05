package udpm.hn.server.core.headdepartment.plan.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface HDPLTutorClassResponse extends IsIdentify, HasOrderNumber {

    String getSubjectId();
    String getSubjectCode();
    String getSubjectName();
    Integer getNumberClasses();
    String getHeadSubject();
    Integer getNumberLectures();
    Integer getFormat();

}
