package udpm.hn.server.core.headsubject.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headsubject.model.request.CreateTutorClassDetailRequest;
import udpm.hn.server.core.headsubject.model.request.TutorClassDetailRequest;
import udpm.hn.server.core.headsubject.model.response.TutorClassDetailResponse;
import udpm.hn.server.core.headsubject.model.response.TutorClassResponse;
import udpm.hn.server.core.headsubject.service.HSTutorClassDetailService;
import udpm.hn.server.core.headsubject.service.HSTutorClassService;
import udpm.hn.server.entity.TutorClass;
import udpm.hn.server.entity.TutorClassDetail;
import udpm.hn.server.repository.TutorClassDetailRepository;
import udpm.hn.server.repository.TutorClassRepository;
import udpm.hn.server.utils.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class HSTutorClassDetailServiceImpl implements HSTutorClassDetailService {

    private final TutorClassDetailRepository tutorClassDetailRepository;
    private final TutorClassRepository tutorClassRepository;

    @Override
    public ResponseObject<?> createTutorClassDetail(CreateTutorClassDetailRequest request) {
        Optional<TutorClass> optionalTutorClass = tutorClassRepository.findById(request.getTutorClassId());
        if (optionalTutorClass.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lớp tutor không tồn tại");
        }
        if (request.getNumberOfClasses() == null || request.getNumberOfClasses() < 0) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Số lượng không hợp lệ");
        }

        List<TutorClassDetail> tutorClassDetails = new ArrayList<>();
        for (int i = 0; i < request.getNumberOfClasses(); i++) {
            TutorClassDetail tutorClassDetail = new TutorClassDetail();
            tutorClassDetail.setTutorClass(optionalTutorClass.get());
            tutorClassDetail.setNumberOfLectures(request.getNumberOfLectures());
            tutorClassDetails.add(tutorClassDetail);
        }

        tutorClassDetailRepository.saveAll(tutorClassDetails);

        return new ResponseObject<>(null, HttpStatus.OK, "Đã tạo chi tiết lớp tutor");
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
