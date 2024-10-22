package udpm.hn.server.core.admin.departments.department.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import udpm.hn.server.core.admin.departments.department.model.request.CreateOrUpdateMajorRequest;
import udpm.hn.server.core.admin.departments.department.model.request.FindMajorRequest;
import udpm.hn.server.core.admin.departments.department.model.response.MajorResponse;
import udpm.hn.server.core.admin.departments.department.repository.DepartmentExtendRepository;
import udpm.hn.server.core.admin.departments.department.repository.MajorExtendRepository;
import udpm.hn.server.core.admin.departments.department.service.MajorService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.superadmin.operation.model.request.OperationLogsRequest;
import udpm.hn.server.core.superadmin.operation.service.OperationLogsService;
import udpm.hn.server.entity.Department;
import udpm.hn.server.entity.Major;
import udpm.hn.server.infrastructure.connection.IdentityConnection;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.FunctionLogType;
import udpm.hn.server.repository.MajorRepository;
import udpm.hn.server.utils.Helper;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class MajorServiceImpl implements MajorService {

    private final MajorExtendRepository majorExtendRepository;

    private final DepartmentExtendRepository departmentExtendRepository;

    private final IdentityConnection identityConnection;

    private final MajorRepository majorRepository;

    private final OperationLogsService logsService;

    @Override
    public ResponseObject<?> getAllMajor(String id, FindMajorRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.SEARCH);
        log.setStatus(true);
        try {
           Pageable pageable = Helper.createPageable(request, "createdDate");
            Page<MajorResponse> majorResponses = majorExtendRepository.getAllMajorByDepartmentIdFilter(id, pageable, request);
            log.setResponse(majorResponses.getContent());
           return new ResponseObject<>(
                   PageableObject.of(majorResponses),
                   HttpStatus.OK,
                   "Lấy thành công danh sách chuyên ngành"
           );
       } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình lấy danh sách chuyên ngành");
       } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> addMajor(@Valid CreateOrUpdateMajorRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.CREATE);
        log.setStatus(true);
        try {
            request.setMajorName(request.getMajorName().replaceAll("\\s+", " "));
            request.setMajorCode(request.getMajorCode().replaceAll("\\s+", " "));
            Optional<Major> existsMajor = majorExtendRepository.findMajorByNameAndDepartmentId(request.getMajorName().trim(), request.getDepartmentId());
            Optional<Department> departmentOptional = departmentExtendRepository.findById(request.getDepartmentId());

            if (departmentOptional.isPresent()) {
                Department currentDepartment = departmentOptional.get();
                if (existsMajor.isEmpty()) {
                    Major addMajor = new Major();
                    addMajor.setName(request.getMajorName().trim());
                    addMajor.setCode(request.getMajorCode().trim());
                    addMajor.setDepartment(currentDepartment);
                    addMajor.setStatus(EntityStatus.ACTIVE);
                    majorRepository.save(addMajor);
                    log.setSuccessMessage("Thêm chuyên ngành vào bộ môn " +
                            currentDepartment.getName() + " thành công");
                    return new ResponseObject<>(null, HttpStatus.CREATED, log.getSuccessMessage());
                } else {
                    log.setStatus(false);
                    log.setErrorMessage("Chuyên ngành đã tồn tại trong bộ môn " +
                            currentDepartment.getName());
                    return new ResponseObject<>(null, HttpStatus.CONFLICT, log.getErrorMessage());
                }
            } else {
                log.setStatus(false);
                log.setErrorMessage("Bộ môn mà bạn đang thêm chuyên " +
                        "ngành không tồn tại [ " + departmentOptional.get().getName() + " ]");
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
            }
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình tạo chuyên ngành");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> updateMajor(@Valid CreateOrUpdateMajorRequest request, String id) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.UPDATE);
        log.setStatus(true);
        try {
            request.setMajorName(request.getMajorName().replaceAll("\\s+", " "));
            request.setMajorCode(request.getMajorCode().replaceAll("\\s+", " "));
            Optional<Major> majorOptional = majorRepository.findById(id);
            Optional<Department> departmentOptional = departmentExtendRepository.findById(request.getDepartmentId());

            if (departmentOptional.isPresent()) {
                Department currentDepartment = departmentOptional.get();
                if (majorOptional.isPresent()) {
                    Major majorUpdate = majorOptional.get();

                    if (!majorUpdate.getName().trim().equalsIgnoreCase(request.getMajorName().trim())) {
                        Optional<Major> existsMajor = majorExtendRepository.findMajorByNameAndDepartmentId(request.getMajorName().trim(), request.getDepartmentId());
                        if (existsMajor.isPresent()) {
                            log.setStatus(false);
                            log.setErrorMessage("Tên chuyên ngành đã tồn tại: " +
                                    request.getMajorName().trim());
                            return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
                        }
                    }

                    majorUpdate.setName(request.getMajorName().trim());
                    majorUpdate.setDepartment(currentDepartment);
                    majorUpdate.setCode(request.getMajorCode().trim());
                    majorRepository.save(majorUpdate);
                    log.setSuccessMessage("Cập nhât chuyên ngành vào bộ môn " +
                            currentDepartment.getName() + " thành công");
                    return new ResponseObject<>(null, HttpStatus.OK, log.getSuccessMessage());
                } else {
                    log.setStatus(false);
                    log.setErrorMessage("Chuyên ngành không tồn tại trong bộ môn " +
                            currentDepartment.getName());
                    return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
                }
            } else {
                log.setStatus(false);
                log.setErrorMessage("Bộ môn mà bạn đang cập nhật chuyên ngành " +
                        "không tồn tại [ " + departmentOptional.get().getName() + " ]");
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
            }
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình cập nhật chuyên ngành");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> deleteMajor(String id) {
        Optional<Major> majorOptional = majorRepository.findById(id);

        if (majorOptional.isPresent()) {
            Major majorDelete = majorOptional.get();

            majorDelete.setStatus(
                    majorDelete.getStatus().name().equalsIgnoreCase(EntityStatus.ACTIVE.name())
                            ? EntityStatus.INACTIVE : EntityStatus.ACTIVE
            );
            majorRepository.save(majorDelete);
            return new ResponseObject<>(null, HttpStatus.OK, "Chuyển đổi thành công chuyên ngành " +
                    majorDelete.getName());
        } else {
            return new ResponseObject<>(null, HttpStatus.OK, "chuyên ngành không tồn tại trong bộ môn");
        }
    }

}