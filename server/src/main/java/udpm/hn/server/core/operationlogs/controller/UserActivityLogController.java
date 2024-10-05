package udpm.hn.server.core.operationlogs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import udpm.hn.server.core.operationlogs.model.request.UserActivityLogFilterRequest;
import udpm.hn.server.core.operationlogs.service.UserActivityLogService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@Controller
@RequestMapping(MappingConstants.API_ADMIN_USER_ACTIVITY)
@RequiredArgsConstructor
public class UserActivityLogController {

    private final UserActivityLogService userActivityLogService;

    @GetMapping
    public ResponseEntity<?> getAllUserActivityLog(UserActivityLogFilterRequest request) {
        return Helper.createResponseEntity(userActivityLogService.getAllUserActivityLog(request));
    }

}
