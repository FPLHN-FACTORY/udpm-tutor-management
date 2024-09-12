package udpm.hn.server.core.admin.staff.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface HOStaffResonpse extends IsIdentify, HasOrderNumber {

    String getStaffName();

    String getStaffCode();

    String getEmailFe();

    String getEmailFpt();

    Long getCreatedDate();

}
