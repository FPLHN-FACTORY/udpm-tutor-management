package udpm.hn.server.core.superadmin.departments.department.service;

import jakarta.validation.Valid;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.superadmin.departments.department.model.request.CreateUpdateDepartmentRequest;
import udpm.hn.server.core.superadmin.departments.department.model.request.FindDepartmentsRequest;

public interface SADepartmentService {


    ResponseObject<?> getAllDepartment(FindDepartmentsRequest request);

    ResponseObject<?> getDetailDepartment(String id);

    ResponseObject<?> addDepartment(@Valid CreateUpdateDepartmentRequest request);

    ResponseObject<?> updateDepartment(@Valid CreateUpdateDepartmentRequest request, String id);

    ResponseObject<?> deleteDepartment(String id);

    ResponseObject<?> synchronize();

    ResponseObject<?> getDepartmentsManagedByStaff(String staffId);

}
