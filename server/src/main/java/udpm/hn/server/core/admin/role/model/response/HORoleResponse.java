package udpm.hn.server.core.admin.role.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface HORoleResponse extends IsIdentify, HasOrderNumber {

    String getRoleCode();

    String getRoleName();

    String getFacilityName();

}
