package udpm.hn.server.core.planner.plan.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface PLPLTutorClassResponse extends IsIdentify, HasOrderNumber {

    String getSubjectId();

    String getSubjectCode();

    String getSubjectName();

    Integer getNumberClasses();

    String getHeadSubject();

    Integer getNumberLectures();

    String getFormat();

}
