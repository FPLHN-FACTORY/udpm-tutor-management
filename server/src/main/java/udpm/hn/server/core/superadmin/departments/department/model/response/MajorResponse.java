package udpm.hn.server.core.superadmin.departments.department.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface MajorResponse extends HasOrderNumber, IsIdentify {

    String getMajorName();

    String getMajorCode();

    Long getMajorStatus();

    Long getCreatedDate();

}