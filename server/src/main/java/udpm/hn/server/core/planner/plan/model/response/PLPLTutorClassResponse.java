package udpm.hn.server.core.planner.plan.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface PLPLTutorClassResponse extends IsIdentify, HasOrderNumber {

    String getSubjectName();
    String getNumberClasses();
    String getFormat();
    String getHeadSubject();

}
