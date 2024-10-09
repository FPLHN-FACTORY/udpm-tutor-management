package udpm.hn.server.core.operationlogs.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.operationlogs.model.request.UserActivityLogFilterRequest;
import udpm.hn.server.core.operationlogs.model.request.UserActivityLogRequest;
import udpm.hn.server.entity.UserActivityLog;

public interface UserActivityLogService {

    ResponseObject<?> getAllUserActivityLog(UserActivityLogFilterRequest request);

    void createUserActivityLog(UserActivityLogRequest userActivityLogRequest);

}
