package udpm.hn.server.core.admin.role.service;

import udpm.hn.server.core.admin.role.model.request.HORoleRequest;
import udpm.hn.server.core.admin.role.model.request.HOSaveRoleRequest;
import udpm.hn.server.core.common.base.ResponseObject;

public interface HORoleService {
    ResponseObject<?> getAllRole(HORoleRequest roleRequest);

    ResponseObject<?> getFacilities();

    ResponseObject<?> saveRole(HOSaveRoleRequest roleRequest);

    ResponseObject<?> getOneRole(String id);

    ResponseObject<?> deleteRole(String id);
}
