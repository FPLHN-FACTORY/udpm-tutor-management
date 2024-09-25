package udpm.hn.server.core.admin.departments.departmentfacility.service;

import jakarta.validation.Valid;
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.CreateMajorFacilityRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.MajorFacilityRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.UpdateMajorFacilityRequest;
import udpm.hn.server.core.common.base.ResponseObject;

public interface MajorFacilityService {

    ResponseObject<?> getAllMajorFacilities(@Valid MajorFacilityRequest request);

    ResponseObject<?> getMajorFacilityById(String majorFacilityId);

    ResponseObject<?> createMajorFacility(@Valid CreateMajorFacilityRequest request);

    ResponseObject<?> updateMajorFacility(String majorFacilityId, @Valid UpdateMajorFacilityRequest request);

    ResponseObject<?> getAllMajors(String departmentId);

    ResponseObject<?> synchronize();

}
