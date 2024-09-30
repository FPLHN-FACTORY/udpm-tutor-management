package udpm.hn.server.core.planner.plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.planner.plan.model.request.PLPLSubjectListRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLTutorClassDetailRequest;
import udpm.hn.server.core.planner.plan.model.response.PLPLTutorClassResponse;
import udpm.hn.server.core.planner.plan.repository.PLPLTutorClassDetailRepository;
import udpm.hn.server.core.planner.plan.repository.PLPLTutorClassRepository;
import udpm.hn.server.core.planner.plan.service.PLPLTutorClassService;
import udpm.hn.server.utils.Helper;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PLPLTutorClassServiceImpl implements PLPLTutorClassService {

    private final PLPLTutorClassRepository tutorClassRepository;

    private final PLPLTutorClassDetailRepository tutorClassDetailRepository;

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
                PageableObject.of(tutorClassRepository.getTutorClasses(pageable, request.getPlanId())
                ), HttpStatus.OK, "Lấy danh sách lớp môn thành công!");
    }

    @Override
    public ResponseObject<?> getTutorClassDetailByTutorClassId(PLPLTutorClassDetailRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(tutorClassDetailRepository.getTutorClassDetailByTutorClassId(pageable, request.getTutorClassId())),
                HttpStatus.OK,
                "Lấy danh sách lớp môn thành công!"
        );
    }

}
