package udpm.hn.server.core.operationlogs.model.request;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.infrastructure.constant.FunctionLogType;

@Getter
@Setter
public class OperationLogsRequest {

    private HttpServletRequest httpRequest;

    private String workstation;

    private String userName;

    private String email;

    private String code;

    private FunctionLogType typeFunction;

    private Object request;

    private Object response;

    private Boolean status;

}
