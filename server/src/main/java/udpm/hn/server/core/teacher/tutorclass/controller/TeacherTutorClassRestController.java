package udpm.hn.server.core.teacher.tutorclass.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.teacher.tutorclass.model.request.TutorClassListRequest;
import udpm.hn.server.core.teacher.tutorclass.service.TutorClassService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_TEACHER_TUTOR_CLASS)
@RequiredArgsConstructor
public class TeacherTutorClassRestController {

    private final TutorClassService tutorClassService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getTutorClassesByTeacher(final TutorClassListRequest request, @PathVariable String id) {
        return Helper.createResponseEntity(tutorClassService.getTutorClassesByTeacher(id, request));
    }

}
