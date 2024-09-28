package udpm.hn.server.core.headsubject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.admin.subject.model.request.CreateUpdateSubjectRequest;
import udpm.hn.server.core.headsubject.model.request.CreateTutorClassRequest;
import udpm.hn.server.core.headsubject.model.request.SHTutorListRequest;
import udpm.hn.server.core.headsubject.model.request.UpdateNumberOfClassesRequest;
import udpm.hn.server.core.headsubject.service.HSTutorClassService;
import udpm.hn.server.core.planner.plan.model.request.PLPLTutorListRequest;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_HEAD_SUBJECT_TUTOR)
@RequiredArgsConstructor
public class HSTutorClassController {

    private final HSTutorClassService hsTutorClassService;

    @GetMapping("/tutor-detail/{id}")
    public ResponseEntity<?> getDetailTutorClass(@PathVariable String id) {
        return Helper.createResponseEntity(hsTutorClassService.getDetailTutorClass(id));
    }

    @GetMapping("/tutor")
    public ResponseEntity<?> getPlans(SHTutorListRequest request) {
        return Helper.createResponseEntity(hsTutorClassService.getTutorClasses(request));
    }

    @PostMapping
    public ResponseEntity<?> createTutorClass(@RequestBody CreateTutorClassRequest request) {
        return Helper.createResponseEntity(hsTutorClassService.createOrUpdateTutorClass(request));
    }

    @PutMapping
    public ResponseEntity<?> updateTutorClass(@RequestBody UpdateNumberOfClassesRequest request) {
        return Helper.createResponseEntity(hsTutorClassService.updateNumberOfClassesTutorClass(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatusApproveTutorClass(@PathVariable String id) {
        return Helper.createResponseEntity(hsTutorClassService.updateStatusApproveClassesTutorClass(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTutorClass(@PathVariable String id) {
        return Helper.createResponseEntity(hsTutorClassService.deleteTutorClass(id));
    }


}
