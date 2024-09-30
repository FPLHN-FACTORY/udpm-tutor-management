package udpm.hn.server.core.headsubject.plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLCreateTutorClassRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLSubjectListRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLTutorClassDetailRequest;
import udpm.hn.server.core.headsubject.plan.service.HSPLTutorClassService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_HEAD_SUBJECT_PLAN)
@RequiredArgsConstructor
public class HSPLTutorClassRestController {

    private final HSPLTutorClassService hsTutorClassService;

    @GetMapping("/tutor")
    public ResponseEntity<?> getTutorClasses(HSPLSubjectListRequest request) {
        return Helper.createResponseEntity(hsTutorClassService.getTutorClasses(request));
    }

    @GetMapping("/tutor/{id}")
    public ResponseEntity<?> getDetailTutorClass(@PathVariable String id) {
        return Helper.createResponseEntity(hsTutorClassService.getDetailTutorClass(id));
    }

    @PutMapping("/tutor")
    public ResponseEntity<?> updateTutorClass(@RequestBody HSPLCreateTutorClassRequest request) {
        return Helper.createResponseEntity(hsTutorClassService.updateNumberOfClassesTutorClass(request));
    }

    @PutMapping("/tutor/{id}")
    public ResponseEntity<?> updateStatusApproveTutorClass(@PathVariable String id) {
        return Helper.createResponseEntity(hsTutorClassService.updateStatusApproveClassesTutorClass(id));
    }

    @GetMapping("/tutor-detail")
    public ResponseEntity<?> getAllTutorClassDetail(HSPLTutorClassDetailRequest request) {
        return Helper.createResponseEntity(hsTutorClassService.getTutorClassDetailByTutorClassId(request));
    }

}
