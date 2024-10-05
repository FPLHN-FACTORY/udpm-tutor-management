package udpm.hn.server.core.planner.plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.planner.plan.model.request.PLPLCreateStudentTutorRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLSubjectListRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLTutorClassDetailRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLUpdateTutorClassDetailRequest;
import udpm.hn.server.core.planner.plan.model.response.PLPLTutorClassResponse;
import udpm.hn.server.core.planner.plan.repository.PLPLStudentTutorClassRepository;
import udpm.hn.server.core.planner.plan.repository.PLPLTutorClassDetailRepository;
import udpm.hn.server.core.planner.plan.repository.PLPLTutorClassRepository;
import udpm.hn.server.core.planner.plan.service.PLPLTutorClassService;
import udpm.hn.server.entity.Block;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.entity.StudentTutor;
import udpm.hn.server.entity.TutorClassDetail;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.PlanStatus;
import udpm.hn.server.utils.Helper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PLPLTutorClassServiceImpl implements PLPLTutorClassService {

    private final PLPLTutorClassRepository tutorClassRepository;

    private final PLPLTutorClassDetailRepository tutorClassDetailRepository;

    private final PLPLStudentTutorClassRepository studentTutorClassRepository;

    @Override
    public ResponseObject<?> getDetailTutorClass(String id) {
        try {
            PLPLTutorClassResponse tutorClass = tutorClassRepository.getDetailTutorClass(id);
            if (tutorClass == null) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Không tồn tại");
            }
            return new ResponseObject<>(tutorClass, HttpStatus.OK, "Lấy lớp tutor thành công1");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lỗi khi lấy chi tiết: " + e.getMessage());
        }
    }

    @Override
    public ResponseObject<?> getTutorClasses(PLPLSubjectListRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(tutorClassRepository.getTutorClasses(pageable, request)
                ), HttpStatus.OK, "Lấy danh sách lớp môn thành công!");
    }

    @Override
    public ResponseObject<?> getTutorClassDetailByTutorClassId(PLPLTutorClassDetailRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(tutorClassDetailRepository.getTutorClassDetailByTutorClassId(pageable, request)),
                HttpStatus.OK,
                "Lấy danh sách lớp môn thành công!"
        );
    }

    @Override
    public ResponseObject<?> updateTutorClassDetail(List<PLPLUpdateTutorClassDetailRequest> request) {
        try {
            // Lọc request chỉ lấy những cái có studentId hoặc room
            List<PLPLUpdateTutorClassDetailRequest> filteredRequest = request.stream()
                    .filter(r -> r.getStudentId() != null || r.getRoom() != null)
                    .toList();

            if (filteredRequest.isEmpty()) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Không có dữ liệu cần cập nhật");
            }

            // Cập nhật cho từng yêu cầu đã lọc
            filteredRequest.forEach(updateRequest -> {
                TutorClassDetail tutorClassDetail = tutorClassDetailRepository.findById(updateRequest.getId()).orElse(null);
                if (tutorClassDetail != null) {
                    // Cập nhật sinh viên nếu có
                    if (updateRequest.getStudentId() != null) {
                        StudentTutor studentTutor = studentTutorClassRepository.findById(updateRequest.getStudentId()).orElse(null);
                        if (studentTutor != null) {
                            tutorClassDetail.setStudentConduct(studentTutor);
                        }
                    }

                    // Cập nhật phòng nếu có
                    if (updateRequest.getRoom() != null) {
                        tutorClassDetail.setRoom(updateRequest.getRoom());
                    }

                    // Lưu lớp tutor
                    tutorClassDetailRepository.save(tutorClassDetail);
                }
            });

            return new ResponseObject<>(true, HttpStatus.OK, "Cập nhật thành công");
        } catch (Exception e) {
            // Xử lý lỗi
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cập nhật thất bại: " + e.getMessage());
        }
    }


    @Override
    public ResponseObject<?> createStudentTutor(PLPLCreateStudentTutorRequest request) {
        Optional<StudentTutor> studentTutorOptional = studentTutorClassRepository.findByStudentCode(request.getCode().trim());
        if(studentTutorOptional.isPresent()){
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Mã sinh viên đã tồn tại!");
        }

        Optional<StudentTutor> studentTutorOptional1 = studentTutorClassRepository.findByStudentEmail(request.getEmail().trim());
        if(studentTutorOptional1.isPresent()){
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Email đã tồn tại!");
        }

        if(!Helper.checkEmail(request.getEmail())){
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Email không đúng định dạng định dạng!");
        }

        StudentTutor studentTutor = new StudentTutor();
        studentTutor.setStudentCode(request.getCode());
        studentTutor.setStudentEmail(request.getEmail());
        studentTutor.setStudentName(request.getName());
        studentTutor.setStatus(EntityStatus.ACTIVE);
        studentTutorClassRepository.save(studentTutor);

        studentTutorClassRepository.save(studentTutor);
        return new ResponseObject<>(null, HttpStatus.CREATED, "Thêm sinh viên thành công thành công");
    }

}
