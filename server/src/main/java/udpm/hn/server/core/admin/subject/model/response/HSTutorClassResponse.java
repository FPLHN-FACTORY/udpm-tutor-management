package udpm.hn.server.core.admin.subject.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface HSTutorClassResponse extends IsIdentify, HasOrderNumber {

    String getSubjectId();
    String getSubjectCode();
    String getSubjectName();
    Integer getNumberClasses();
    String getHeadSubject();
    Integer getNumberLectures();
    Integer getFormat();

}