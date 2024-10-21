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
import udpm.hn.server.core.superadmin.operation.model.request.OperationLogsRequest;
import udpm.hn.server.core.superadmin.operation.service.OperationLogsService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Department;
import udpm.hn.server.entity.OperationLog;
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

    private final OperationLogsService operationLogsService;

    @Override
    public ResponseObject<?> getAllDepartment(FindDepartmentsRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        Page<DDepartmentResponse> departmentResponses = departmentExtendRepository.getAllDepartmentByFilter(pageable, request);
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest operationLogsRequest = new OperationLogsRequest();
        operationLogsRequest.setHttpRequest(httpServletRequest);
        operationLogsRequest.setRequest(request);
        operationLogsRequest.setResponse(departmentResponses.getContent());
        operationLogsRequest.setTypeFunction(FunctionLogType.SEARCH);
        operationLogsRequest.setStatus(true);
        OperationLog operationLog = operationLogsService.createOperationLog(operationLogsRequest);
        if(operationLog==null){
            return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Đã xảy ra lỗi khi lưu log");
        }
        return new ResponseObject<>(
                PageableObject.of(departmentResponses),
                HttpStatus.OK,
                "Lấy thành công danh sách bộ môn"
        );
    }

    @Override
    public ResponseObject<?> getDetailDepartment(String id) {
        return new ResponseObject<>(departmentExtendRepository.getDetailDepartment(id), HttpStatus.OK, "Lấy thành công bộ môn");
    }

    @Override
    public ResponseObject<?> getDepartmentsManagedByStaff(String staffId) {
        return new ResponseObject<>(departmentExtendRepository.getDepartmentsManagedByStaff(staffId),  HttpStatus.OK, "Lấy thành công danh sách bộ môn theo trưởng môn");
    }


    @Override
    public ResponseObject<?> addDepartment(@Valid CreateUpdateDepartmentRequest request) {
        String code = Helper.generateCodeFromName((request.getDepartmentCode().trim()));
        request.setDepartmentName(request.getDepartmentName().replaceAll("\\s+", " "));
        request.setDepartmentCode(code);

        boolean checkExistName = departmentExtendRepository.existsByName(request.getDepartmentName().trim());
        boolean checkExistCode = departmentExtendRepository.existsByCode(code);

        if (checkExistCode) {
            return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Mã bộ môn đã tồn tại");
        }

        if (checkExistName) {
            return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Tên bộ môn đã tồn tại");
        }

        Department department = new Department();
        department.setStatus(EntityStatus.ACTIVE);
        department.setCode(code);
        department.setName(request.getDepartmentName().trim());
        this.departmentExtendRepository.save(department);
        return new ResponseObject<>(null, HttpStatus.CREATED, "Thêm thành công bộ môn");
    }

    @Override
    public ResponseObject<?> updateDepartment(@Valid CreateUpdateDepartmentRequest request, String id) {
        String code = Helper.generateCodeFromName((request.getDepartmentCode().trim()));
        request.setDepartmentName(request.getDepartmentName().replaceAll("\\s+", " "));
        request.setDepartmentCode(code);

        Optional<Department> departmentOptional = departmentExtendRepository.findById(id);

        if (departmentOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Bộ môn không tồn tại");
        }

        Department updateDepartment = departmentOptional.get();
        if (!updateDepartment.getCode().trim().equalsIgnoreCase(code)) {
            if (departmentExtendRepository.existsByCode(code)) {
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Mã bộ môn đã tồn tại: " +
                        code);
            }
        }
        if (!updateDepartment.getName().trim().equalsIgnoreCase(request.getDepartmentName().trim())) {
            if (departmentExtendRepository.existsByName(request.getDepartmentName().trim())) {
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Tên bộ môn đã tồn tại: " +
                        request.getDepartmentName().trim());
            }
        }

        updateDepartment.setCode(code);
        updateDepartment.setName(request.getDepartmentName().trim());
        this.departmentExtendRepository.save(updateDepartment);
        return new ResponseObject<>(null, HttpStatus.OK, "Cập nhật bộ môn thành công");
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

    @Override
    @Transactional
    public ResponseObject<?> synchronize() {
        OperationLogsRequest operationLogsRequest = new OperationLogsRequest();
        try {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            operationLogsRequest.setHttpRequest(httpServletRequest);
            operationLogsRequest.setTypeFunction(FunctionLogType.SYNCHRONIZED);
            List<DepartmentResponse> departmentData = identityConnection.getDepartments();
            List<Department> departments = departmentExtendRepository.findAll();
            if (departments.isEmpty()) {
                for (DepartmentResponse departmentResponse : departmentData) {
                    syncDepartment(null, departmentResponse);
                }
            } else {
                for (DepartmentResponse departmentResponse : departmentData) {
                    for (Department department : departments) {
                        syncDepartment(department, departmentResponse);
                    }
                }
            }
            operationLogsRequest.setRequest(departments);
            operationLogsRequest.setResponse(departmentData);
            operationLogsRequest.setStatus(true);
            return ResponseObject.successForward(null, "Đồng bộ bộ môn thành công!");
        } catch (Exception e) {
            operationLogsRequest.setStatus(false);
            operationLogsRequest.setResponse("{\"status\": \"false\"}");
            log.error("Lỗi khi đồng bộ bộ môn : {}", e.getMessage());
            return ResponseObject.errorForward("Đồng bộ bộ môn không thành công! Đã xảy ra lỗi.", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            operationLogsService.createOperationLog(operationLogsRequest);
        }
    }

    private void syncDepartment(Department department, DepartmentResponse departmentResponse) {
        Department postDepartment;
        if (department == null) {
            postDepartment = new Department();
        } else {
            Optional<Department> departmentOptional = departmentExtendRepository.findDepartmentByDepartmentIdentityId(
                    departmentResponse.getDepartmentId());
            postDepartment = departmentOptional.orElseGet(Department::new);
        }
        postDepartment.setName(departmentResponse.getDepartmentName());
        postDepartment.setCode(departmentResponse.getDepartmentCode());
        postDepartment.setDepartmentIdentityId(departmentResponse.getDepartmentId());
        departmentExtendRepository.save(postDepartment);
    }


}