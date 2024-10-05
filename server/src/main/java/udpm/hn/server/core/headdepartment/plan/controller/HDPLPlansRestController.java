package udpm.hn.server.core.headdepartment.plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.headdepartment.plan.model.request.HDPLPlanInfoRequest;
import udpm.hn.server.core.headdepartment.plan.model.request.HDPLPlanListRequest;
import udpm.hn.server.core.headdepartment.plan.service.HDPLPlansService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_HEAD_DEPARTMENT_HEAD_PLAN)
@RequiredArgsConstructor
public class HDPLPlansRestController {

    private final HDPLPlansService planService;

    @GetMapping
    public ResponseEntity<?> getPlans(HDPLPlanListRequest request) {
        return Helper.createResponseEntity(planService.getPlans(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlanDetail(@PathVariable String id) {
        return Helper.createResponseEntity(planService.getPlansDetail(id));
    }

    @GetMapping("/semester")
    public ResponseEntity<?> getPlanBySemester(HDPLPlanInfoRequest request) {
        return Helper.createResponseEntity(planService.getPlansBySemester(request));
    }

    @GetMapping("/info")
    public ResponseEntity<?> getPlanInfo(HDPLPlanInfoRequest request) {
        return Helper.createResponseEntity(planService.getPlansInfo(request));
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getPlanInfo(@PathVariable String id) {
        return Helper.createResponseEntity(planService.getPlansInfoById(id));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> approvePlan(@PathVariable String id) {
        return Helper.createResponseEntity(planService.approvePlan(id));
    }

}
