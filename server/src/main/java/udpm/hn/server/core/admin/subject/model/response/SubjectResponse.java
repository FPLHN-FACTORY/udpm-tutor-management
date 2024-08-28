package udpm.hn.server.core.admin.subject.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface SubjectResponse extends IsIdentify, HasOrderNumber {

    String getSubjectCode();

    String getSubjectName();

    String getDepartmentName();

    String getSubjectType();

    String getSubjectStatus();

    Long getCreatedDate();

}
