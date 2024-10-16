package udpm.hn.server.core.superadmin.activity.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.superadmin.activity.model.request.UserActivityLogFilterRequest;
import udpm.hn.server.core.superadmin.activity.model.request.UserActivityLogRequest;

public interface UserActivityLogService {

    ResponseObject<?> getAllUserActivityLog(UserActivityLogFilterRequest request);

    void createUserActivityLog(UserActivityLogRequest userActivityLogRequest);

}
