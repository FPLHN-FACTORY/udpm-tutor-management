package udpm.hn.server.core.superadmin.departments.department.model.response;


import org.springframework.beans.factory.annotation.Value;
import udpm.hn.server.core.common.base.IsIdentify;

public interface ListDepartmentResponse extends IsIdentify {

    @Value("#{target.departmentName}")
    String getDepartmentName();

}