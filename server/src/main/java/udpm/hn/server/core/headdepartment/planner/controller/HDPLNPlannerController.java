package udpm.hn.server.core.headdepartment.planner.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.headdepartment.planner.model.request.HDPLNAssignedPlannerRequest;
import udpm.hn.server.core.headdepartment.planner.model.request.HDPLNPlannerListRequest;
import udpm.hn.server.core.headdepartment.planner.service.HDPLNPlannerService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_HEAD_DEPARTMENT_PLANNER)
@RequiredArgsConstructor
public class HDPLNPlannerController {

    private final HDPLNPlannerService plannerService;

    @GetMapping
    public ResponseEntity<?> getAllPlannerManager(HDPLNPlannerListRequest request) {
        return Helper.createResponseEntity(plannerService.getAllPlannerManager(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> isAssignedPlanner(@PathVariable String id, @RequestBody HDPLNAssignedPlannerRequest request) {
        return Helper.createResponseEntity(plannerService.isAssignedPlanner(id, request));
    }

    @PutMapping("/unassigned/{id}")
    public ResponseEntity<?> unAssignPlanner(@PathVariable String id) {
        return Helper.createResponseEntity(plannerService.unAssignPlanner(id));
    }

}
