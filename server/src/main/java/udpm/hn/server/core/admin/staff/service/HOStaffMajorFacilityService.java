package udpm.hn.server.core.admin.staff.service;

import jakarta.validation.Valid;
import udpm.hn.server.core.admin.staff.model.request.HOStaffMajorFacilityRequest;
import udpm.hn.server.core.common.base.ResponseObject;

public interface HOStaffMajorFacilityService {

    ResponseObject<?> getStaffMajorFacilities(String staffId);

    ResponseObject<?> getDepartmentByFacility(String idFacility);

    ResponseObject<?> getMajorByDepartmentFacility(String idDepartment, String idFacility);

    ResponseObject<?> createStaffMajorFacility(@Valid HOStaffMajorFacilityRequest staffMajorFacilityRequest);

    ResponseObject<?> updateStaffMajorFacility(@Valid HOStaffMajorFacilityRequest staffMajorFacilityRequest);

    ResponseObject<?> deleteStaffMajorFacility(String idStaffMajorFacility);

    ResponseObject<?> detailStaffMajorFacility(String idStaffMajorFacility);

}
