package udpm.hn.server.core.headdepartment.headplans.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.headdepartment.headplans.model.request.HeadPlanRequest;
import udpm.hn.server.core.headdepartment.headplans.service.HeadPlansService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_HEAD_DEPARTMENT_HEAD_PLAN)
@RequiredArgsConstructor
public class HeadPlansRestController {

    private final HeadPlansService headPlansService;

    @GetMapping
    public ResponseEntity<?> getPlans(HeadPlanRequest request) {
        return Helper.createResponseEntity(headPlansService.getPlans(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> approvePlan(@PathVariable String id) {
        return Helper.createResponseEntity(headPlansService.approvePlan(id));
    }

}
