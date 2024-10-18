package udpm.hn.server.core.teacher.student.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.core.teacher.student.model.request.StudentAttendanceRequest;
import udpm.hn.server.core.teacher.student.service.TCSStudentService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

import java.util.List;

@RestController
@RequestMapping(MappingConstants.API_TEACHER_STUDENT_ATTENDANCE)
@RequiredArgsConstructor
public class TCSStudentAttendanceController {

    private final TCSStudentService studentService;

    @GetMapping("/{lectureId}")
    public ResponseEntity<?> getStudentByLectureId(@PathVariable String lectureId) {
        return Helper.createResponseEntity(studentService.getStudentByLectureId(lectureId));
    }

    @PutMapping("/attendance")
    public ResponseEntity<?> createOrUpdateAttendance(@RequestBody List<StudentAttendanceRequest> request) {
        return Helper.createResponseEntity(studentService.createAttendance(request));
    }

}
