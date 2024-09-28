package udpm.hn.server.core.admin.subject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.admin.subject.model.request.CreateUpdateSubjectRequest;
import udpm.hn.server.core.admin.subject.model.request.SubjectRequest;
import udpm.hn.server.core.admin.subject.service.SubjectService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_SUBJECT)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SubjectRestController {

    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<?> getAll(SubjectRequest request) {
        return Helper.createResponseEntity(subjectService.getAllSubject(request));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateUpdateSubjectRequest request) {
        return Helper.createResponseEntity(subjectService.createSubject(request));
    }

    @PutMapping("/{subjectId}")
    public ResponseEntity<?> update(@PathVariable String subjectId, @RequestBody CreateUpdateSubjectRequest request) {
        return Helper.createResponseEntity(subjectService.updateSubject(subjectId, request));
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<?> getById(@PathVariable String subjectId) {
        return Helper.createResponseEntity(subjectService.getSubjectById(subjectId));
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<?> getAllSubjectByStaffId(@PathVariable String staffId) {
        return Helper.createResponseEntity(subjectService.getAllSubjectByStaffId(staffId));
    }

}
