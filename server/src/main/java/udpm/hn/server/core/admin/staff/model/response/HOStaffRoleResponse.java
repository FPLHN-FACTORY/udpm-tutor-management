package udpm.hn.server.core.admin.staff.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface HOStaffRoleResponse extends HasOrderNumber {

    String getRoleName();

    String getRoleCode();

    String getRoleId();

    String getFacilityName();

}
