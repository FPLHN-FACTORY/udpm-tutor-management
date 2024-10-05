package udpm.hn.server.core.operationlogs.service;

import udpm.hn.server.core.operationlogs.model.request.OperationLogsFilterRequest;
import udpm.hn.server.core.operationlogs.model.request.OperationLogsRequest;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.OperationLog;

public interface OperationLogsService {
    OperationLog createOperationLog(OperationLogsRequest request);

    ResponseObject<?> getDetailOperationLog(String id);

    ResponseObject<?> getAllOperationLog(OperationLogsFilterRequest request);

}
