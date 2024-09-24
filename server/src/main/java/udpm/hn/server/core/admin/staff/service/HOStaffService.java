package udpm.hn.server.core.admin.staff.service;

import udpm.hn.server.core.admin.staff.model.request.HOSaveStaffRequest;
import udpm.hn.server.core.admin.staff.model.request.HOStaffRequest;
import udpm.hn.server.core.common.base.ResponseObject;

public interface HOStaffService {

    ResponseObject<?> getStaffByRole(HOStaffRequest hoRoleStaffRequest);

    ResponseObject<?> getDepartmentFacility();

    ResponseObject<?> detailStaff(String staffId);

    ResponseObject<?> createStaff(HOSaveStaffRequest staffRequest);

    ResponseObject<?> updateStaff(HOSaveStaffRequest staffRequest);

    ResponseObject<?> deleteStaff(String idStaff);

    ResponseObject<?> synchronize(String campusCode);

}
