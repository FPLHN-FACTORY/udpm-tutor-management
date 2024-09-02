package udpm.hn.server.core.admin.semester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import udpm.hn.server.core.admin.semester.model.request.CreateUpdateSemesterRequest;
import udpm.hn.server.core.admin.semester.model.request.SemesterRequest;
import udpm.hn.server.core.admin.semester.service.SemesterService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@Controller
@RequestMapping(MappingConstants.REDIRECT_ADMIN_SEMESTER)
@RequiredArgsConstructor
public class SemesterController {

    private final SemesterService semesterService;

    @PostMapping("/all")
    public ResponseEntity<?> getAllSemesters(@RequestBody SemesterRequest request) {
        return ResponseEntity.ok(semesterService.getAllSemester(request));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateUpdateSemesterRequest createUpdateSemesterRequest) {
        return Helper.createResponseEntity(semesterService.createSemester(createUpdateSemesterRequest));
    }

    @GetMapping("/{semesterId}")
    public ResponseEntity<?> getSemesterById(@PathVariable String semesterId) {
        return Helper.createResponseEntity(semesterService.getSemesterById(semesterId));
    }

    @PutMapping("/{semesterId}")
    public ResponseEntity<?> update(@PathVariable String semesterId, @RequestBody CreateUpdateSemesterRequest createUpdateSemesterRequest) {
        return Helper.createResponseEntity(semesterService.updateSemester(semesterId, createUpdateSemesterRequest));
    }

    @PutMapping("/status/{semesterId}")
    public ResponseEntity<?> statusChangeSemester(@PathVariable String semesterId) {
        return Helper.createResponseEntity(semesterService.statusChangeSemester(semesterId));
    }


}
