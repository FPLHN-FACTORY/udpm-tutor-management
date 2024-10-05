package udpm.hn.server.core.operationlogs.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import udpm.hn.server.core.operationlogs.model.request.OperationLogsFilterRequest;
import udpm.hn.server.core.operationlogs.service.OperationLogsService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@Controller
@RequestMapping(MappingConstants.API_ADMIN_OPERATION_LOG)
@RequiredArgsConstructor
public class OperationLogsController {

    private final OperationLogsService operationLogsService;

    @GetMapping
    public ResponseEntity<?> getAllOperationLogs(OperationLogsFilterRequest request) {
        return Helper.createResponseEntity(operationLogsService.getAllOperationLog(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailOperationLog(@PathVariable String id) {
        return Helper.createResponseEntity(operationLogsService.getDetailOperationLog(id));
    }

}
