package udpm.hn.server.core.headsubject.plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.HeadSubjectRequest;
import udpm.hn.server.core.headsubject.plan.model.request.AssignedPlannerRequest;
import udpm.hn.server.core.headsubject.plan.model.request.CreatePlannerRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLCreateTutorClassRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLPlanInfoRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLPlanListRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLPlannerListRequest;
import udpm.hn.server.core.headsubject.plan.service.HSPLPlanService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_HEAD_SUBJECT_PLAN)
@RequiredArgsConstructor
public class HSPLPlansRestController {

    private final HSPLPlanService planService;

    @GetMapping
    public ResponseEntity<?> getPlans(HSPLPlanListRequest request) {
        return Helper.createResponseEntity(planService.getAllPlans(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlanDetail(@PathVariable String id) {
        return Helper.createResponseEntity(planService.getPlansDetail(id));
    }

    @GetMapping("/semester")
    public ResponseEntity<?> getPlanBySemester(HSPLPlanInfoRequest request) {
        return Helper.createResponseEntity(planService.getPlansBySemester(request));
    }

    @GetMapping("/info")
    public ResponseEntity<?> getPlanInfo(HSPLPlanInfoRequest request) {
        return Helper.createResponseEntity(planService.getPlansInfo(request));
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getPlanInfo(@PathVariable String id) {
        return Helper.createResponseEntity(planService.getPlansInfoById(id));
    }

    @GetMapping("/planner")
    public ResponseEntity<?> getAllPlannerManager(HSPLPlannerListRequest request) {
        return Helper.createResponseEntity(planService.getAllPlannerManager(request));
    }

    @PutMapping("/planner/{id}")
    public ResponseEntity<?> isAssignedPlanner(@PathVariable String id, @RequestBody AssignedPlannerRequest request) {
        return Helper.createResponseEntity(planService.isAssignedPlanner(id, request));
    }

    @PostMapping("/planner")
    public ResponseEntity<?> createPlanner(@RequestBody CreatePlannerRequest request) {
        return Helper.createResponseEntity(planService.createPlanner(request));
    }

}
