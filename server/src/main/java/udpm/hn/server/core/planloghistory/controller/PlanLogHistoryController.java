package udpm.hn.server.core.planloghistory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import udpm.hn.server.core.planloghistory.model.request.PlanLogHistoryFilterRequest;
import udpm.hn.server.core.planloghistory.service.PlanLogHistoryService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@Controller
@RequestMapping(MappingConstants.API_PLAN_LOG_HISTORY)
@RequiredArgsConstructor
public class PlanLogHistoryController {

    private final PlanLogHistoryService planLogHistoryService;

    @GetMapping
    public ResponseEntity<?> getAllPlanLogHistory(PlanLogHistoryFilterRequest request) {
        return Helper.createResponseEntity(planLogHistoryService.getAllPlanLogHistory(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailPlanLogHistory(@PathVariable String id) {
        return Helper.createResponseEntity(planLogHistoryService.getDetailPlanLogHistory(id));
    }

}
