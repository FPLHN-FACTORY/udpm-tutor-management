package udpm.hn.server.core.superadmin.activity.model.response;

import udpm.hn.server.core.common.base.HasOrderNumber;
import udpm.hn.server.core.common.base.IsIdentify;

public interface OperationLogsResponse extends IsIdentify, HasOrderNumber {
    String getWorkstation();
    String getUserName();
    String getApi();

    String getEmail();

    String getCode();

    Integer getTypeFunction();

    String getRequest();

    String getResponse();

    String getStatus();

    Long getCreatedDate();
}
