package udpm.hn.server.core.headsubject.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headsubject.model.request.CreateTutorClassRequest;
import udpm.hn.server.core.headsubject.model.request.SHTutorListRequest;
import udpm.hn.server.core.headsubject.model.request.UpdateNumberOfClassesRequest;
import udpm.hn.server.core.headsubject.model.response.TutorClassResponse;
import udpm.hn.server.core.headsubject.service.HSTutorClassService;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.entity.Subject;
import udpm.hn.server.entity.TutorClass;
import udpm.hn.server.entity.TutorClassDetail;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.PlanFormat;
import udpm.hn.server.infrastructure.constant.TutorClassStatus;
import udpm.hn.server.repository.PlanRepository;
import udpm.hn.server.repository.SubjectRepository;
import udpm.hn.server.repository.TutorClassDetailRepository;
import udpm.hn.server.repository.TutorClassRepository;
import udpm.hn.server.utils.Helper;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class HSTutorClassServiceImpl implements HSTutorClassService {

    private final SubjectRepository subjectRepository;

    private final PlanRepository planRepository;

    private final TutorClassRepository tutorClassRepository;

    private final TutorClassDetailRepository tutorClassDetailRepository;

    @Override
    public ResponseObject<?> createOrUpdateTutorClass(CreateTutorClassRequest request) {

        Optional<Subject> optionalSubject = subjectRepository.findById(request.getSubjectId());
        Optional<Plan> optionalPlan = planRepository.findById(request.getPlaneId());

        if (optionalSubject.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Môn học không tồn tại");
        }
        if (optionalPlan.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch không tồn tại");
        }
        if (request.getNumberOfClasses() == null || request.getNumberOfClasses() < 0) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Số lượng lớp học không hợp lệ");
        }

        TutorClass tutorClass = new TutorClass();
        tutorClass.setPlan(optionalPlan.get());
        tutorClass.setSubject(optionalSubject.get());
        tutorClass.setNumberOfClasses(request.getNumberOfClasses());
        if (request.getPlanFormat() == 1) {
            tutorClass.setPlanFormat(PlanFormat.OFFLINE);
        } else {
            tutorClass.setPlanFormat(PlanFormat.ONLINE);
        }

        try {
            tutorClassRepository.save(tutorClass);
            return new ResponseObject<>(tutorClass, HttpStatus.OK, "Thêm lớp thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Thêm lớp không thành công: " + e.getMessage());
        }
    }

    @Override
    public ResponseObject<?> updateNumberOfClassesTutorClass(UpdateNumberOfClassesRequest request) {
        try {
            TutorClass tutorClass = tutorClassRepository.findById(request.getTutorClassId()).orElse(null);
            if (tutorClass == null) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Id không hợp lệ");
            }
            tutorClass.setNumberOfClasses(request.getNumberOfClasses());
            tutorClassRepository.save(tutorClass);
            return new ResponseObject<>(tutorClass, HttpStatus.OK, "Cập nhật số lượng lớp tutor thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cập nhật số lượng lớp tutor không thành công: " + e.getMessage());
        }
    }

    @Override
    public ResponseObject<?> updateStatusApproveClassesTutorClass(String id) {
        try {
            TutorClass tutorClass = tutorClassRepository.findById(id).orElse(null);
            if (tutorClass == null) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Id không hợp lệ");
            }
            tutorClass.setStatus(EntityStatus.ACTIVE);
            tutorClassRepository.save(tutorClass);
            // Lưu tutor detail dựa vào số lương
            if (tutorClass.getNumberOfClasses() != null && tutorClass.getNumberOfClasses() > 0) {
                for (int i = 0; i < tutorClass.getNumberOfClasses(); i++) {
                    TutorClassDetail tutorClassDetail = new TutorClassDetail();
                    tutorClassDetail.setTutorClass(tutorClass);
                    tutorClassDetail.setNumberOfLectures(8);
                    tutorClassDetailRepository.save(tutorClassDetail);
                }
            }
            return new ResponseObject<>(tutorClass, HttpStatus.OK, "Cập nhật trạng thái lớp tutor thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cập nhật trạng thái lớp tutor không thành công: " + e.getMessage());
        }
    }

    @Override
    public ResponseObject<?> deleteTutorClass(String id) {
        try {
            TutorClass tutorClass = tutorClassRepository.findById(id).orElse(null);
            if (tutorClass == null) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Id không hợp lệ");
            }
            tutorClassRepository.delete(tutorClass);
            return new ResponseObject<>(tutorClass, HttpStatus.OK, "Xóa số lượng lớp tutor thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Xóa số lượng lớp tutor không thành công: " + e.getMessage());
        }
    }

    @Override
    public ResponseObject<?> getDetailTutorClass(String id) {
        try {
            TutorClassResponse tutorClass = tutorClassRepository.getDetailTutorClass(id);
            if (tutorClass == null) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Không tồn tại");
            }
            return new ResponseObject<>(tutorClass, HttpStatus.OK, "Lấy lớp tutor thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lỗi khi lấy chi tiết: " + e.getMessage());
        }
    }

    @Override
    public ResponseObject<?> getTutorClasses(SHTutorListRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(tutorClassRepository.getTutorClasses(pageable, request.getPlanId(), request.getDepartmentCode(), request.getFacilityCode(), request.getSemesterId())),
                HttpStatus.OK,
                "Lấy danh sách lớp môn thành công!"
        );
    }

}
