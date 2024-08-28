package udpm.hn.server.core.admin.departments.department.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.admin.departments.department.model.request.CreateUpdateDepartmentRequest;
import udpm.hn.server.core.admin.departments.department.model.request.FindDepartmentsRequest;
import udpm.hn.server.core.admin.departments.department.repository.DepartmentExtendRepository;
import udpm.hn.server.core.admin.departments.department.service.DepartmentService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Department;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.utils.Helper;

import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentExtendRepository departmentExtendRepository;

    @Override
    public ResponseObject<?> getAllDepartment(FindDepartmentsRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(departmentExtendRepository.getAllDepartmentByFilter(pageable, request)),
                HttpStatus.OK,
                "Lấy thành công danh sách bộ môn"
        );
    }

    @Override
    public ResponseObject<?> getDetailDepartment(String id) {
        return new ResponseObject<>(departmentExtendRepository.getDetailDepartment(id), HttpStatus.OK, "Lấy thành công bộ môn");
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
    public ResponseObject<?> getListDepartment() {
        return new ResponseObject<>(departmentExtendRepository.getListDepartment(), HttpStatus.OK, "Lấy thành công danh sách bộ môn");
    }

}