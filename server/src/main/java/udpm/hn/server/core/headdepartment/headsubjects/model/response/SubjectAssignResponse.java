package udpm.hn.server.core.headdepartment.headsubjects.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface SubjectAssignResponse extends IsIdentify, HasOrderNumber {

    String getSubjectCode();

    String getSubjectName();

    String getSubjectType();

    Short getIsAssigned();

}
