package udpm.hn.server.core.planner.plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.planner.plan.model.request.PLPLSubjectListRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLTutorClassDetailRequest;
import udpm.hn.server.core.planner.plan.service.PLPLTutorClassService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_PLANNER_PLAN)
@RequiredArgsConstructor
public class TutorClassRestController {

    private final PLPLTutorClassService tutorClassService;

    @GetMapping("/tutor")
    public ResponseEntity<?> getTutorClasses(PLPLSubjectListRequest request) {
        return Helper.createResponseEntity(tutorClassService.getTutorClasses(request));
    }

    @GetMapping("/tutor/{id}")
    public ResponseEntity<?> getDetailTutorClass(@PathVariable String id) {
        return Helper.createResponseEntity(tutorClassService.getDetailTutorClass(id));
    }

    @GetMapping("/tutor-detail")
    public ResponseEntity<?> getAllTutorClassDetail(PLPLTutorClassDetailRequest request) {
        return Helper.createResponseEntity(tutorClassService.getTutorClassDetailByTutorClassId(request));
    }

}