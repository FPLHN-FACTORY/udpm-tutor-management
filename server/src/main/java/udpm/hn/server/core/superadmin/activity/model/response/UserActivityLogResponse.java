package udpm.hn.server.core.superadmin.activity.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface UserActivityLogResponse extends IsIdentify, HasOrderNumber {

    String getWorkstation();

    String getEmail();

    String getName();

    String getCode();

    String getStatus();

    String getSessionId();

    String getOperation();

    Long getLoginTime();

    Long getLogoutTime();

}
