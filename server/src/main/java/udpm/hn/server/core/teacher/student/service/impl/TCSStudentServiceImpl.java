package udpm.hn.server.core.teacher.student.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.teacher.student.model.request.StudentAttendanceRequest;
import udpm.hn.server.core.teacher.student.model.response.StudentAttendanceResponse;
import udpm.hn.server.core.teacher.student.repository.TCSAttendanceRepository;
import udpm.hn.server.core.teacher.student.repository.TCSStudentRepository;
import udpm.hn.server.core.teacher.student.service.TCSStudentService;
import udpm.hn.server.entity.Attendance;
import udpm.hn.server.repository.LectureRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TCSStudentServiceImpl implements TCSStudentService {

    private final TCSStudentRepository studentRepository;

    private final TCSAttendanceRepository tcsAttendanceRepository;

    private final LectureRepository lectureRepository;

    @Override
    public ResponseObject<?> getStudentByLectureId(String lectureId) {
        List<StudentAttendanceResponse> studentAttendanceResponses = studentRepository.getListStudentAttendance(lectureId);
        return new ResponseObject<>(
                studentAttendanceResponses,
                HttpStatus.OK,
                "Lấy list lectures thành công."
        );
    }

    @Override
    public ResponseObject<?> createAttendance(List<StudentAttendanceRequest> request) {

        if (request.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Không có dữ liệu để điểm danh");
        }
        Long dateTime = System.currentTimeMillis();
        for (StudentAttendanceRequest attendanceRequest : request) {
            var studentOptional = studentRepository.findById(attendanceRequest.getStudentId());
            if (studentOptional.isEmpty()) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Không tìm thấy sinh viên với ID: " + attendanceRequest.getStudentId());
            }

            var lectureOptional = lectureRepository.findById(attendanceRequest.getLectureId());
            if (lectureOptional.isEmpty()) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Không tìm thấy buổi học với ID: " + attendanceRequest.getLectureId());
            }

            Optional<Attendance> attendanceOptional = tcsAttendanceRepository
                    .findAttendanceByStudentIdAndLectureId(studentOptional.get().getId(), lectureOptional.get().getId());

            Attendance attendance;
            if (attendanceOptional.isPresent()) {
                attendance = attendanceOptional.get();
                attendance.setIsPresent(attendanceRequest.getIsPresent());
                attendance.setCreatedDate(dateTime);
                attendance.setNote(attendance.getNote());
            } else {
                attendance = new Attendance();
                attendance.setStudent(studentOptional.get());
                attendance.setLecture(lectureOptional.get());
                attendance.setIsPresent(attendanceRequest.getIsPresent());
                attendance.setCreatedDate(dateTime);
                attendance.setNote(attendance.getNote());
            }
            tcsAttendanceRepository.save(attendance);
        }

        return new ResponseObject<>(null, HttpStatus.OK, "Điểm danh thành công.");
    }

}
