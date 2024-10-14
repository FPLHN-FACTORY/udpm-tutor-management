package udpm.hn.server.core.planner.plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.planner.plan.model.request.PLPLCreateGoogleFormPlanRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLCreatePlanRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLPlanInfoRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLPlanListRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLUpdatePlanRequest;
import udpm.hn.server.core.planner.plan.service.PLPLPlansService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_PLANNER_PLAN)
@RequiredArgsConstructor
public class PlansRestController {

    private final PLPLPlansService planService;

    @GetMapping
    public ResponseEntity<?> getPlans(PLPLPlanListRequest request) {
        return Helper.createResponseEntity(planService.getAllPlans(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlanDetail(@PathVariable String id) {
        return Helper.createResponseEntity(planService.getPlansDetail(id));
    }

    @GetMapping("/semester")
    public ResponseEntity<?> getPlanBySemester(PLPLPlanInfoRequest request) {
        return Helper.createResponseEntity(planService.getPlansBySemester(request));
    }

    @GetMapping("/info")
    public ResponseEntity<?> getPlanInfo(PLPLPlanInfoRequest request) {
        return Helper.createResponseEntity(planService.getPlansInfo(request));
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getPlanInfo(@PathVariable String id) {
        return Helper.createResponseEntity(planService.getPlansInfoById(id));
    }

    @PostMapping
    public ResponseEntity<?> createPlan(@RequestBody PLPLCreatePlanRequest request) {
        return Helper.createResponseEntity(planService.createPlan(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlan(@RequestBody PLPLUpdatePlanRequest request, @PathVariable String id) {
        return Helper.createResponseEntity(planService.updatePlan(id, request));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> approvePlan(@PathVariable String id) {
        return Helper.createResponseEntity(planService.approvePlan(id));
    }

    @PutMapping("/start/{id}")
    public ResponseEntity<?> startPlan(@PathVariable String id) {
        return Helper.createResponseEntity(planService.startPlan(id));
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<?> checkApprovePlan(@PathVariable String id) {
        return Helper.createResponseEntity(planService.checkApprovePlan(id));
    }
}
