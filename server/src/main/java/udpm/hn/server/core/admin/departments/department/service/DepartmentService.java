package udpm.hn.server.core.admin.departments.department.service;

import jakarta.validation.Valid;
import udpm.hn.server.core.admin.departments.department.model.request.CreateUpdateDepartmentRequest;
import udpm.hn.server.core.admin.departments.department.model.request.FindDepartmentsRequest;
import udpm.hn.server.core.common.base.ResponseObject;

public interface DepartmentService {


    ResponseObject<?> getAllDepartment(FindDepartmentsRequest request);

    ResponseObject<?> getDetailDepartment(String id);

    ResponseObject<?> addDepartment(@Valid CreateUpdateDepartmentRequest request);

    ResponseObject<?> updateDepartment(@Valid CreateUpdateDepartmentRequest request, String id);

    ResponseObject<?> deleteDepartment(String id);

}
