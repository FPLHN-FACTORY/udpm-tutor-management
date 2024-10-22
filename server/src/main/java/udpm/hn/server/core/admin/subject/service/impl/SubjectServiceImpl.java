package udpm.hn.server.core.admin.subject.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import udpm.hn.server.core.admin.subject.model.request.CreateUpdateSubjectRequest;
import udpm.hn.server.core.admin.subject.model.request.SubjectRequest;
import udpm.hn.server.core.admin.subject.model.response.SubjectResponse;
import udpm.hn.server.core.admin.subject.repository.DepartmentSubjectRepository;
import udpm.hn.server.core.admin.subject.repository.SubjectExtendRepository;
import udpm.hn.server.core.admin.subject.service.SubjectService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.superadmin.operation.model.request.OperationLogsRequest;
import udpm.hn.server.core.superadmin.operation.service.OperationLogsService;
import udpm.hn.server.entity.Department;
import udpm.hn.server.entity.Subject;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.FunctionLogType;
import udpm.hn.server.infrastructure.constant.SubjectType;
import udpm.hn.server.utils.Helper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class SubjectServiceImpl implements SubjectService {

    private final SubjectExtendRepository subjectExtendRepository;

    private final DepartmentSubjectRepository departmentSubjectRepository;

    private final OperationLogsService logsService;

    @Override
    public ResponseObject<?> getAllSubject(SubjectRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.SEARCH);
        log.setStatus(true);
        try {
            Pageable pageable = Helper.createPageable(request);
            Page<SubjectResponse> responses = subjectExtendRepository.getAllSubject(pageable, request);
            log.setResponse(responses.getContent());
            return new ResponseObject<>(
                    PageableObject.of(responses),
                    HttpStatus.OK,
                    "Lấy danh sách môn học thành công"
            );
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình lấy danh sách môn học");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> createSubject(@Valid CreateUpdateSubjectRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.CREATE);
        log.setStatus(true);
        try {
            Optional<Subject> subjectOptional = subjectExtendRepository.findByCode(request.getSubjectCode());
            if (subjectOptional.isPresent()) {
                log.setStatus(false);
                log.setErrorMessage("Mã môn học đã tồn tại");
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, log.getErrorMessage());
            }

            Optional<Department> department = departmentSubjectRepository.findById(request.getDepartmentId());
            if (department.isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("Bộ môn không tồn tại");
                return new ResponseObject<>(null, HttpStatus.NOT_FOUND, log.getErrorMessage());
            }

            Subject subject = new Subject();
            subject.setName(request.getSubjectName());
            subject.setCode(request.getSubjectCode());
            subject.setDepartment(department.get());
            subject.setSubjectType(SubjectType.valueOf(request.getSubjectType()));
            subject.setCode(request.getSubjectCode());
            subject.setCreatedTime(request.getStartDate());
            subject.setStatus(EntityStatus.ACTIVE);
            Subject subjectSaved = subjectExtendRepository.save(subject);
            log.setSuccessMessage("Tạo môn học thành công");
            return new ResponseObject<>(null, HttpStatus.CREATED, log.getErrorMessage());
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình thêm môn học");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> updateSubject(String subjectId, @Valid CreateUpdateSubjectRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.UPDATE);
        log.setStatus(true);

        try {
            Department department = departmentSubjectRepository.findById(request.getDepartmentId()).orElse(null);
            if (department == null) {
                log.setStatus(false);
                log.setErrorMessage("Bộ môn không tồn tại");
                return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Bộ môn không tồn tại");
            }


            Optional<Subject> subjectOptional = subjectExtendRepository.findById(subjectId)
                    .map(subject -> {
                        subject.setName(request.getSubjectName());
                        subject.setCode(request.getSubjectCode());
                        subject.setDepartment(department);
                        subject.setSubjectType(SubjectType.valueOf(request.getSubjectType()));
                        subject.setCreatedTime(request.getStartDate());
                        subject.setStatus(EntityStatus.ACTIVE);
                        return subjectExtendRepository.save(subject);
                    });

            if (subjectOptional.isPresent()) {
                log.setSuccessMessage("Cập nhật môn học thành công");
                return new ResponseObject<>(null, HttpStatus.OK, log.getSuccessMessage());
            } else {
                log.setStatus(false);
                log.setErrorMessage("Môn học không tồn tại");
                return new ResponseObject<>(null, HttpStatus.NOT_FOUND, log.getErrorMessage());
            }

        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình cập nhật môn học");
        } finally {
            logsService.createOperationLog(log);
        }
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
