package udpm.hn.server.core.headsubject.plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headsubject.plan.model.request.CreateTutorClassRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLSubjectListRequest;
import udpm.hn.server.core.headsubject.plan.model.request.TutorClassDetailRequest;
import udpm.hn.server.core.headsubject.plan.model.response.TutorClassResponse;
import udpm.hn.server.core.headsubject.plan.repository.HSPLPlansRepository;
import udpm.hn.server.core.headsubject.plan.repository.HSPLSubjectRepository;
import udpm.hn.server.core.headsubject.plan.repository.HSPLTutorClassRepository;
import udpm.hn.server.core.headsubject.plan.service.HSPLTutorClassService;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.entity.Subject;
import udpm.hn.server.entity.TutorClass;
import udpm.hn.server.entity.TutorClassDetail;
import udpm.hn.server.repository.TutorClassDetailRepository;
import udpm.hn.server.utils.Helper;

import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class HSPLTutorClassServiceImpl implements HSPLTutorClassService {

    private final HSPLSubjectRepository subjectRepository;

    private final HSPLPlansRepository planRepository;

    private final HSPLTutorClassRepository tutorClassRepository;

    private final TutorClassDetailRepository tutorClassDetailRepository;

    @Override
    public ResponseObject<?> updateNumberOfClassesTutorClass(CreateTutorClassRequest request) {
        try {
            Plan plan = planRepository.findById(request.getPlaneId()).orElse(null);
            Subject subject = subjectRepository.findById(request.getSubjectId()).orElse(null);
            TutorClass tutorClass = tutorClassRepository.findByPlanAndSubject(plan, subject).orElse(null);
            if (tutorClass == null) {
                    tutorClass = new TutorClass();
                    tutorClass.setPlan(plan); // Giả định bạn có thuộc tính này
                    tutorClass.setSubject(subject); // Giả định bạn có thuộc tính này
                    tutorClass.setNumberOfClasses(request.getNumberOfClasses());
                    tutorClass.setTutorClassStatus(0);
                    tutorClassRepository.save(tutorClass);
                    return new ResponseObject<>(tutorClass, HttpStatus.CREATED, "Tạo mới lớp tutor thành công");
            } else {
                // Nếu tồn tại, cập nhật số lượng lớp
                tutorClass.setNumberOfClasses(request.getNumberOfClasses());
                tutorClassRepository.save(tutorClass);
                return new ResponseObject<>(tutorClass, HttpStatus.OK, "Cập nhật số lượng lớp tutor thành công");
            }
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
            tutorClass.setTutorClassStatus(1);
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
    public ResponseObject<?> getDetailTutorClass(String id) {
        try {
            TutorClassResponse tutorClass = tutorClassRepository.getDetailTutorClass(id);
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
    public ResponseObject<?> getTutorClasses(HSPLSubjectListRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(tutorClassRepository.getTutorClasses(pageable, request)
                ), HttpStatus.OK, "Lấy danh sách lớp môn thành công!");
    }

    @Override
    public ResponseObject<?> getTutorClassDetailByTutorClassId(TutorClassDetailRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(tutorClassDetailRepository.getTutorClassDetailByTutorClassId(pageable, request.getTutorClassId())),
                HttpStatus.OK,
                "Lấy danh sách lớp môn thành công!"
        );
    }

}
