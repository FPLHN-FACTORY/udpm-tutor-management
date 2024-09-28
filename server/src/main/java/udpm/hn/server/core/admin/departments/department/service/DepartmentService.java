package udpm.hn.server.core.admin.departments.department.service;

import jakarta.validation.Valid;
import udpm.hn.server.core.admin.departments.department.model.request.CreateUpdateDepartmentRequest;
import udpm.hn.server.core.admin.departments.department.model.request.FindDepartmentsRequest;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Department;

import java.util.List;

public interface DepartmentService {


    ResponseObject<?> getAllDepartment(FindDepartmentsRequest request);

    ResponseObject<?> getDetailDepartment(String id);

    ResponseObject<?> addDepartment(@Valid CreateUpdateDepartmentRequest request);

    ResponseObject<?> updateDepartment(@Valid CreateUpdateDepartmentRequest request, String id);

    ResponseObject<?> deleteDepartment(String id);

    ResponseObject<?> synchronize();

    ResponseObject<?> getDepartmentsManagedByStaff(String staffId);

}
