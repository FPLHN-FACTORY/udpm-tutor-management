package udpm.hn.server.core.operationlogs.model.response;

import jakarta.persistence.Column;
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
