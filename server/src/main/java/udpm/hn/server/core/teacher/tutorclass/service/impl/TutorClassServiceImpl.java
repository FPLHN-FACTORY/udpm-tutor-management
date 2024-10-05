package udpm.hn.server.core.teacher.tutorclass.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.teacher.tutorclass.model.request.TutorClassListRequest;
import udpm.hn.server.core.teacher.tutorclass.repository.TeacherExtendRepository;
import udpm.hn.server.core.teacher.tutorclass.repository.TutorClassExtendRepository;
import udpm.hn.server.core.teacher.tutorclass.service.TutorClassService;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.utils.Helper;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TutorClassServiceImpl implements TutorClassService {

    private final TutorClassExtendRepository tutorClassRepository;

    private final TeacherExtendRepository teacherRepository;

    @Override
    public ResponseObject<?> getTutorClassesByTeacher(String teacherId, TutorClassListRequest request) {
        log.info("Teacher id: " +  teacherId);
        log.info("Request: " + request.toString());
        Optional<Staff> staffOptional = teacherRepository.findById(teacherId);
        if (staffOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Giảng viên không tồn tại");
        }
        Pageable pageable = Helper.createPageable(request, "createDate");
        return new ResponseObject<>(
                PageableObject.of(tutorClassRepository.getTutorClassByTeacher(pageable, request, teacherId)),
                HttpStatus.OK,
                "Lấy list thành công."
        );
    }
}
