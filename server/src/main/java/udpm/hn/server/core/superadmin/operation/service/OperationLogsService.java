package udpm.hn.server.core.superadmin.operation.service;

import udpm.hn.server.core.superadmin.operation.model.request.OperationLogsFilterRequest;
import udpm.hn.server.core.superadmin.operation.model.request.OperationLogsRequest;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.OperationLog;

public interface OperationLogsService {
    OperationLog createOperationLog(OperationLogsRequest request);

    ResponseObject<?> getDetailOperationLog(String id);

    ResponseObject<?> getAllOperationLog(OperationLogsFilterRequest request);

}
