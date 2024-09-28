package udpm.hn.server.core.admin.subject.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.admin.subject.model.request.CreateUpdateSubjectRequest;
import udpm.hn.server.core.admin.subject.model.request.SubjectRequest;
import udpm.hn.server.core.admin.subject.repository.DepartmentSubjectRepository;
import udpm.hn.server.core.admin.subject.repository.SubjectExtendRepository;
import udpm.hn.server.core.admin.subject.service.SubjectService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Department;
import udpm.hn.server.entity.Subject;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.SubjectType;
import udpm.hn.server.utils.Helper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class SubjectServiceImpl implements SubjectService {

    private final SubjectExtendRepository subjectExtendRepository;

    private final DepartmentSubjectRepository departmentSubjectRepository;

    @Override
    public ResponseObject<?> getAllSubject(SubjectRequest request) {
        Pageable pageable = Helper.createPageable(request);
        return new ResponseObject<>(
                PageableObject.of(subjectExtendRepository.getAllSubject(pageable, request)),
                HttpStatus.OK,
                "Lấy danh sách môn học thành công"
        );
    }

    @Override
    public ResponseObject<?> createSubject(@Valid CreateUpdateSubjectRequest request) {

        Optional<Subject> subjectOptional = subjectExtendRepository.findBySubjectCode(request.getSubjectCode());
        if (subjectOptional.isPresent()) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Mã môn học đã tồn tại");
        }

        Optional<Department> department = departmentSubjectRepository.findById(request.getDepartmentId());
        if (department.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Bộ môn không tồn tại");
        }

        Subject subject = new Subject();
        subject.setName(request.getSubjectName());
        subject.setSubjectCode(request.getSubjectCode());
        subject.setDepartment(department.get());
        subject.setSubjectType(SubjectType.valueOf(request.getSubjectType()));
        subject.setSubjectCode(request.getSubjectCode());
        subject.setCreatedTime(request.getStartDate());
        subject.setStatus(EntityStatus.ACTIVE);
        Subject subjectSaved = subjectExtendRepository.save(subject);

        return new ResponseObject<>(null, HttpStatus.CREATED, "Tạo môn học thành công");
    }

    @Override
    public ResponseObject<?> updateSubject(String subjectId, @Valid CreateUpdateSubjectRequest request) {

        Department department = departmentSubjectRepository.findById(request.getDepartmentId()).orElse(null);
        if (department == null) {
            return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Bộ môn không tồn tại");
        }


        Optional<Subject> subjectOptional = subjectExtendRepository.findById(subjectId)
                .map(subject -> {
                    subject.setName(request.getSubjectName());
                    subject.setSubjectCode(request.getSubjectCode());
                    subject.setDepartment(department);
                    subject.setSubjectType(SubjectType.valueOf(request.getSubjectType()));
                    subject.setCreatedTime(request.getStartDate());
                    subject.setStatus(EntityStatus.ACTIVE);
                    return subjectExtendRepository.save(subject);
                });

        return subjectOptional
                .map(subject -> new ResponseObject<>(null, HttpStatus.OK, "Cập nhật môn học thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Môn học không tồn tại"));
    }

    @Override
    public ResponseObject<?> changeSubjectStatus(String subjectId) {
        return null;
    }

    @Override
    public ResponseObject<?> getSubjectById(String subjectId) {
        return subjectExtendRepository.getDetailSubjectById(subjectId)
                .map(subject -> new ResponseObject<>(subject, HttpStatus.OK, "Lấy thông tin môn học thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Môn học không tồn tại"));
    }

    @Override
    public ResponseObject<?> getAllSubjectByStaffId(String staffId) {
        return new ResponseObject<>(subjectExtendRepository.getAllSubjectByStaffId(staffId),
                HttpStatus.OK,
                "Lấy danh sách môn học thành công"
        );
    }

}
