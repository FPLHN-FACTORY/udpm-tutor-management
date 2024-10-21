package udpm.hn.server.core.superadmin.departments.departmentfacility.service;

import jakarta.validation.Valid;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.superadmin.departments.departmentfacility.model.request.CreateOrUpdateDepartmentFacilityRequest;
import udpm.hn.server.core.superadmin.departments.departmentfacility.model.request.FindFacilityDetailRequest;

public interface SADepartmentFacilityService {

    ResponseObject<?> getAllDepartmentFacility(String id, FindFacilityDetailRequest request);

    ResponseObject<?> addDepartmentFacility(@Valid CreateOrUpdateDepartmentFacilityRequest request);

    ResponseObject<?> updateDepartmentFacility(@Valid CreateOrUpdateDepartmentFacilityRequest request, String id);

    ResponseObject<?> deleteDepartmentFacility(String id);

    ResponseObject<?> getListFacility();

    ResponseObject<?> getListStaff();

    ResponseObject<?> getDepartmentName(String departmentId);

    ResponseObject<?> synchronize();

}
