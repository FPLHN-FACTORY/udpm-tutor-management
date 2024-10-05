package udpm.hn.server.core.headsubject.plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLCreateTutorClassRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLSubjectListRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLTutorClassDetailRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLUpdateTutorClassDetailRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLUpdateTutorClassRequest;
import udpm.hn.server.core.headsubject.plan.service.HSPLTutorClassService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.infrastructure.security.user.UserPrincipal;
import udpm.hn.server.utils.Helper;

import static udpm.hn.server.utils.UserContextHelper.getCurrentUserId;

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

    @PostMapping("/tutor")
    public ResponseEntity<?> createTutorClass(@RequestBody HSPLCreateTutorClassRequest request) {
        return Helper.createResponseEntity(hsTutorClassService.createTutorClass(request));
    }

    @PutMapping("/tutor/{id}")
    public ResponseEntity<?> updateTutorClass(@PathVariable String id, @RequestBody HSPLUpdateTutorClassRequest request) {
        return Helper.createResponseEntity(hsTutorClassService.updateTutorClass(id, request));
    }

    @GetMapping("/tutor-detail")
    public ResponseEntity<?> getAllTutorClassDetail(HSPLTutorClassDetailRequest request) {
        return Helper.createResponseEntity(hsTutorClassService.getTutorClassDetail(request));
    }

    @PutMapping("/tutor-detail/{id}")
    public ResponseEntity<?> updateTutorClassDetail(@PathVariable("id") String id, @RequestBody HSPLUpdateTutorClassDetailRequest request) {
        return Helper.createResponseEntity(hsTutorClassService.updateTutorClassDetail(id, request));
    }

    @DeleteMapping("/tutor-detail/{id}")
    public ResponseEntity<?> deleteTutorClassDetail(@PathVariable("id") String id) {
        return Helper.createResponseEntity(hsTutorClassService.deleteTutorClassDetail(id));
    }

    @PostMapping("/tutor-detail/{id}")
    public ResponseEntity<?> postTutorClassDetail(@PathVariable("id") String id) {
        return Helper.createResponseEntity(hsTutorClassService.addTutorClassDetail(id));
    }

}
