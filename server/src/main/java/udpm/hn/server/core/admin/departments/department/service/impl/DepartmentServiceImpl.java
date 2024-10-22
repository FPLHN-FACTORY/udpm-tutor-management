package udpm.hn.server.core.admin.departments.department.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import udpm.hn.server.core.admin.departments.department.model.request.CreateUpdateDepartmentRequest;
import udpm.hn.server.core.admin.departments.department.model.request.FindDepartmentsRequest;
import udpm.hn.server.core.admin.departments.department.model.response.DDepartmentResponse;
import udpm.hn.server.core.admin.departments.department.repository.DepartmentExtendRepository;
import udpm.hn.server.core.admin.departments.department.service.DepartmentService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.superadmin.operation.model.request.OperationLogsRequest;
import udpm.hn.server.core.superadmin.operation.service.OperationLogsService;
import udpm.hn.server.entity.Department;
import udpm.hn.server.infrastructure.connection.IdentityConnection;
import udpm.hn.server.infrastructure.connection.response.DepartmentResponse;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.FunctionLogType;
import udpm.hn.server.utils.Helper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentExtendRepository departmentExtendRepository;

    private final IdentityConnection identityConnection;

    private final OperationLogsService logsService;

    @Override
    public ResponseObject<?> getAllDepartment(FindDepartmentsRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.SEARCH);
        log.setStatus(true);

        try {
            Pageable pageable = Helper.createPageable(request, "createdDate");
            Page<DDepartmentResponse> departmentResponses = departmentExtendRepository.getAllDepartmentByFilter(pageable, request);
            log.setResponse(departmentResponses.getContent());
            return new ResponseObject<>(
                    PageableObject.of(departmentResponses),
                    HttpStatus.OK,
                    "Lấy thành công danh sách bộ môn"
            );
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình lấy danh sách bộ môn");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> getDetailDepartment(String id) {
        return new ResponseObject<>(departmentExtendRepository.getDetailDepartment(id), HttpStatus.OK, "Lấy thành công bộ môn");
    }

    @Override
    public ResponseObject<?> getDepartmentsManagedByStaff(String staffId) {
        return new ResponseObject<>(departmentExtendRepository.getDepartmentsManagedByStaff(staffId), HttpStatus.OK, "Lấy thành công danh sách bộ môn theo trưởng môn");
    }


    @Override
    public ResponseObject<?> addDepartment(@Valid CreateUpdateDepartmentRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.CREATE);
        log.setStatus(true);

        try {
            String code = Helper.generateCodeFromName(request.getDepartmentCode().trim());
            request.setDepartmentName(request.getDepartmentName().replaceAll("\\s+", " "));
            request.setDepartmentCode(code);

            boolean checkExistName = departmentExtendRepository.existsByName(request.getDepartmentName().trim());
            boolean checkExistCode = departmentExtendRepository.existsByCode(code);

            if (checkExistCode) {
                log.setStatus(false);
                log.setErrorMessage("Mã bộ môn đã tồn tại");
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
            }

            if (checkExistName) {
                log.setStatus(false);
                log.setErrorMessage("Tên bộ môn đã tồn tại");
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
            }

            Department department = new Department();
            department.setStatus(EntityStatus.ACTIVE);
            department.setCode(code);
            department.setName(request.getDepartmentName().trim());
            this.departmentExtendRepository.save(department);
            log.setSuccessMessage("Thêm thành công bộ môn");
            return new ResponseObject<>(null, HttpStatus.CREATED, log.getSuccessMessage());

        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình thêm bộ môn");

        } finally {
            try {
                logsService.createOperationLog(log);
            } catch (Exception logError) {
                logError.printStackTrace();
            }
        }

    }

    @Override
    public ResponseObject<?> updateDepartment(@Valid CreateUpdateDepartmentRequest request, String id) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.UPDATE);
        log.setStatus(true);

        try {
            String code = Helper.generateCodeFromName((request.getDepartmentCode().trim()));
            request.setDepartmentName(request.getDepartmentName().replaceAll("\\s+", " "));
            request.setDepartmentCode(code);

            Optional<Department> departmentOptional = departmentExtendRepository.findById(id);

            if (departmentOptional.isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("Bộ môn không tồn tại");
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
            }

            Department updateDepartment = departmentOptional.get();
            if (!updateDepartment.getCode().trim().equalsIgnoreCase(code)) {
                if (departmentExtendRepository.existsByCode(code)) {
                    log.setStatus(false);
                    log.setErrorMessage("Mã bộ môn đã tồn tại: " + code);
                    return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
                }
            }
            if (!updateDepartment.getName().trim().equalsIgnoreCase(request.getDepartmentName().trim())) {
                if (departmentExtendRepository.existsByName(request.getDepartmentName().trim())) {
                    log.setStatus(false);
                    log.setErrorMessage("Tên bộ môn đã tồn tại: " + request.getDepartmentName().trim());
                    return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
                }
            }

            updateDepartment.setCode(code);
            updateDepartment.setName(request.getDepartmentName().trim());
            this.departmentExtendRepository.save(updateDepartment);
            log.setSuccessMessage("Cập nhật bộ môn thành công");
            return new ResponseObject<>(null, HttpStatus.OK, log.getSuccessMessage());
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình cập nhật bộ môn");
        } finally {
            try {
                logsService.createOperationLog(log);
            } catch (Exception logError) {
                logError.printStackTrace();
            }
        }
    }

    @Override
    public ResponseObject<?> deleteDepartment(String id) {
        Optional<Department> departmentOptional = departmentExtendRepository.findById(id);

        if (departmentOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Bộ môn không tồn tại");
        }

        Department deleteDepartment = departmentOptional.get();
        deleteDepartment.setStatus(
                deleteDepartment.getStatus().name().equalsIgnoreCase(EntityStatus.ACTIVE.name())
                        ? EntityStatus.INACTIVE : EntityStatus.ACTIVE
        );

        this.departmentExtendRepository.save(deleteDepartment);
        return new ResponseObject<>(null, HttpStatus.OK, "Xóa bộ môn thành công");
    }
}