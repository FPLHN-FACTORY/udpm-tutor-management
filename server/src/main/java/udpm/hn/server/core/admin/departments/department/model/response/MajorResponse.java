package udpm.hn.server.core.admin.departments.department.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;

public interface MajorResponse extends HasOrderNumber {

    String getMajorId();

    String getMajorName();

    Long getMajorStatus();

    Long getCreatedDate();

}