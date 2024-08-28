package udpm.hn.server.core.admin.staff.service;

import udpm.hn.server.core.admin.staff.model.request.HOStaffRoleChangePermissionRequest;
import udpm.hn.server.core.admin.staff.model.request.HOStaffRoleRequest;
import udpm.hn.server.core.common.base.ResponseObject;

public interface HOStaffRoleService {

    ResponseObject<?> getAllRole(String staffId);

    ResponseObject<?> getRolesChecked(HOStaffRoleRequest roleRequest);

    ResponseObject<?> updateStaffRole(HOStaffRoleChangePermissionRequest request);

    ResponseObject<?> getFacilities();

    ResponseObject<?> getFacilitiesSelect(String idStaff);

}
