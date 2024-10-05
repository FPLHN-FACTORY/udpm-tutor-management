package udpm.hn.server.core.operationlogs.model.request;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;
import udpm.hn.server.infrastructure.constant.EntityStatus;

@Getter
@Setter
public class OperationLogsFilterRequest extends PageableRequest {
    private String emailOrNameOrCode;
    private String typeFunction;
    private Long fromDate;
    private Long toDate;
    private EntityStatus status;
}
