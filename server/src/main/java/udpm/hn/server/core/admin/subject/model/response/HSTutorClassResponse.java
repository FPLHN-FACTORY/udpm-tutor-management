package udpm.hn.server.core.admin.subject.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface HSTutorClassResponse extends IsIdentify, HasOrderNumber {

    String getSubjectName();
    String getNumberClasses();
    String getFormat();
    String getHeadSubject();

    Long getStatus();

}