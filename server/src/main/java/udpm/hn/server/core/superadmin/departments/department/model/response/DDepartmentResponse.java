package udpm.hn.server.core.superadmin.departments.department.model.response;

import org.springframework.beans.factory.annotation.Value;
import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface DDepartmentResponse extends IsIdentify, HasOrderNumber {

    @Value("#{target.departmentName}")
    String getDepartmentName();

    @Value("#{target.departmentCode}")
    String getDepartmentCode();

    @Value("#{target.departmentStatus}")
    Long getDepartmentStatus();

    @Value("#{target.createdDate}")
    Long getCreatedDate();

}