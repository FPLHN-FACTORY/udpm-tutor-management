package udpm.hn.server.core.superadmin.departments.departmentfacility.service;

import jakarta.validation.Valid;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.superadmin.departments.departmentfacility.model.request.CreateMajorFacilityRequest;
import udpm.hn.server.core.superadmin.departments.departmentfacility.model.request.MajorFacilityRequest;
import udpm.hn.server.core.superadmin.departments.departmentfacility.model.request.UpdateMajorFacilityRequest;

public interface SAMajorFacilityService {

    ResponseObject<?> getAllMajorFacilities(@Valid MajorFacilityRequest request);

    ResponseObject<?> getMajorFacilityById(String majorFacilityId);

    ResponseObject<?> createMajorFacility(@Valid CreateMajorFacilityRequest request);

    ResponseObject<?> updateMajorFacility(String majorFacilityId, @Valid UpdateMajorFacilityRequest request);

    ResponseObject<?> getAllMajors(String departmentId);

    ResponseObject<?> synchronize();

}
