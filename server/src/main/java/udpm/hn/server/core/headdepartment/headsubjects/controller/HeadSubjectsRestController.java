package udpm.hn.server.core.headdepartment.headsubjects.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.AssignOrUnAssignSubjectForHeadSubjectRequest;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.HeadSubjectRequest;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.HeadSubjectSearchRequest;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.ReassignHeadSubjectRequest;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.SubjectByHeadSubjectRequest;
import udpm.hn.server.core.headdepartment.headsubjects.service.HeadSubjectsService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_HEAD_DEPARTMENT_HEAD_SUBJECT)
@RequiredArgsConstructor
public class HeadSubjectsRestController {

    private final HeadSubjectsService headSubjectsService;

    @GetMapping
    public ResponseEntity<?> getHeadSubjects(HeadSubjectRequest request) {
        return Helper.createResponseEntity(headSubjectsService.getAllHeadSubjects(request));
    }

    @GetMapping("/{headSubjectId}/subjects")
    public ResponseEntity<?> getSubjectsByHeadSubject(
            @PathVariable String headSubjectId,
            SubjectByHeadSubjectRequest request
    ) {
        return Helper.createResponseEntity(headSubjectsService.getSubjectsByHeadSubject(headSubjectId, request));
    }

    @GetMapping("/{headSubjectId}/subjects/assign")
    public ResponseEntity<?> getSubjectsWithAssign(
            @PathVariable String headSubjectId,
            SubjectByHeadSubjectRequest request
    ) {
        return Helper.createResponseEntity(headSubjectsService.getSubjectsWithAssign(headSubjectId, request));
    }

    @PutMapping("/{headSubjectId}/subjects/assign")
    public ResponseEntity<?> assignSubjectForHeadSubject(
            @PathVariable String headSubjectId,
            @RequestBody AssignOrUnAssignSubjectForHeadSubjectRequest request
    ) {
        return Helper.createResponseEntity(headSubjectsService.assignSubjectForHeadSubject(headSubjectId, request));
    }

    @DeleteMapping("/{headSubjectId}/subjects/assign")
    public ResponseEntity<?> unassignSubjectForHeadSubject(
            @PathVariable String headSubjectId,
            @RequestBody AssignOrUnAssignSubjectForHeadSubjectRequest request
    ) {
        return Helper.createResponseEntity(headSubjectsService.unassignSubjectForHeadSubject(headSubjectId, request));
    }

    @PutMapping("/subjects/reassign")
    public ResponseEntity<?> reassignSubjectForAnotherHeadSubject(
            @RequestBody ReassignHeadSubjectRequest request
    ) {
        return Helper.createResponseEntity(headSubjectsService.reassignSubjectForAnotherHeadSubject(request));
    }

    @GetMapping("/staff/search")
    public ResponseEntity<?> searchStaff(HeadSubjectSearchRequest request) {
        return Helper.createResponseEntity(headSubjectsService.searchStaff(request));
    }

    @GetMapping("/subjects/sync")
    public ResponseEntity<?> syncHeadSubjectAttachWithSubjectFromPreviousSemesterToCurrentSemester() {
        return Helper.createResponseEntity(
                headSubjectsService.syncHeadSubjectAttachWithSubjectFromPreviousSemesterToCurrentSemester()
        );
    }

    @GetMapping("/subjects/can-sync")
    public ResponseEntity<?> checkCurrentSemesterHasHeadSubject() {
        return Helper.createResponseEntity(headSubjectsService.checkCurrentSemesterHasHeadSubject());
    }

}
