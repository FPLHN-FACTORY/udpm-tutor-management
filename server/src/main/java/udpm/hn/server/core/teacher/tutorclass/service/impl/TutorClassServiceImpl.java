package udpm.hn.server.core.teacher.tutorclass.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.planner.plan.model.request.PLPLUpdateTutorClassDetailRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCUpdateTutorClassDetailRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TutorClassListRequest;
import udpm.hn.server.core.teacher.tutorclass.repository.TcTcTutorClassDetailExtendRepository;
import udpm.hn.server.core.teacher.tutorclass.repository.TeacherExtendRepository;
import udpm.hn.server.core.teacher.tutorclass.repository.TutorClassExtendRepository;
import udpm.hn.server.core.teacher.tutorclass.service.TutorClassService;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.StudentTutor;
import udpm.hn.server.entity.TutorClassDetail;
import udpm.hn.server.infrastructure.constant.Shift;
import udpm.hn.server.utils.Helper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TutorClassServiceImpl implements TutorClassService {

    private final TutorClassExtendRepository tutorClassRepository;
    private final TeacherExtendRepository teacherRepository;
    private final TcTcTutorClassDetailExtendRepository tutorClassDetailExtendRepository;

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

    @Override
    public ResponseObject<?> updateTutorClassDetail(List<TCTCUpdateTutorClassDetailRequest> request) {
        try {
            // Lọc request chỉ lấy những cái có studentId hoặc room
            List<TCTCUpdateTutorClassDetailRequest> filteredRequest = request.stream()
                    .filter(r -> r.getShift() != null || r.getStartTime() != null)
                    .toList();

            if (filteredRequest.isEmpty()) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Không có dữ liệu cần cập nhật");
            }

            // Cập nhật cho từng yêu cầu đã lọc
            filteredRequest.forEach(updateRequest -> {
                TutorClassDetail tutorClassDetail = tutorClassDetailExtendRepository.findById(updateRequest.getId()).orElse(null);
                if (tutorClassDetail != null) {
                    // Cập nhật sinh viên nếu có
                    if (updateRequest.getShift() != null) {
                        Shift shift = Shift.fromString(updateRequest.getShift());
                        tutorClassDetail.setShift(shift);
                    }
                    if (updateRequest.getStartTime() != null) {
                        tutorClassDetail.setStartDate(updateRequest.getStartTime());
                    }
                    if (updateRequest.getEndTime() != null) {
                        tutorClassDetail.setEndDate(updateRequest.getEndTime());
                    }
                    // Lưu lớp tutor
                    tutorClassDetailExtendRepository.save(tutorClassDetail);
                }
            });

            return new ResponseObject<>(true, HttpStatus.OK, "Cập nhật thành công");
        } catch (Exception e) {
            // Xử lý lỗi
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cập nhật thất bại: " + e.getMessage());
        }
    }
}
