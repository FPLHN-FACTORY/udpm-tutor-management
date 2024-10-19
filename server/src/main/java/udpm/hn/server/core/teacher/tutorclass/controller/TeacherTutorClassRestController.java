package udpm.hn.server.core.teacher.tutorclass.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCCurrentPlanRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCEvidenceRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCUpdateLectureRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCUpdateTutorClassDetailRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCTutorClassListRequest;
import udpm.hn.server.core.teacher.tutorclass.service.TCTCTutorClassService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;
import java.util.List;

@RestController
@RequestMapping(MappingConstants.API_TEACHER_TUTOR_CLASS)
@RequiredArgsConstructor
public class TeacherTutorClassRestController {

    private final TCTCTutorClassService tutorClassService;

    @GetMapping
    public ResponseEntity<?> getTutorClassesByTeacher(final TCTCTutorClassListRequest request) {
        return Helper.createResponseEntity(tutorClassService.getTutorClassesByTeacher(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLecturesByTutorClassDetailId(@PathVariable String id) {
        return Helper.createResponseEntity(tutorClassService.getLecturesByTutorClassDetailId(id));
    }

    @GetMapping("/infor/{id}")
    public ResponseEntity<?> getTutorClassDetail(@PathVariable String id) {
        return Helper.createResponseEntity(tutorClassService.getTutorClassDetail(id));
    }

    @GetMapping("/lecture/{id}")
    public ResponseEntity<?> getEvidenceLectureDetail(@PathVariable String id) {
        return Helper.createResponseEntity(tutorClassService.getEvidenceLectureDetail(id));
    }

    @PutMapping
    public ResponseEntity<?> updateShift(@RequestBody List<TCTCUpdateTutorClassDetailRequest> request) {
        return Helper.createResponseEntity(tutorClassService.updateTutorClassDetail(request));
    }

    @PutMapping("/lecture")
    public ResponseEntity<?> updateLecture(@RequestBody List<TCTCUpdateLectureRequest> list) {
        return Helper.createResponseEntity(tutorClassService.updateLecture(list));
    }

    @PutMapping("/lecture-evidence")
    public ResponseEntity<?> addLectureEvidence(@ModelAttribute TCTCEvidenceRequest request) {
        return Helper.createResponseEntity(tutorClassService.evidenceLecture(request));
    }

    @GetMapping("/plan")
    public ResponseEntity<?> getPlan(TCTCCurrentPlanRequest request) {
        return Helper.createResponseEntity(tutorClassService.getPlan(request));
    }

}
