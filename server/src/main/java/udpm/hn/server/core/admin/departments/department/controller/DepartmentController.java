package udpm.hn.server.core.admin.departments.department.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import udpm.hn.server.core.admin.departments.department.model.request.CreateUpdateDepartmentRequest;
import udpm.hn.server.core.admin.departments.department.model.request.FindDepartmentsRequest;
import udpm.hn.server.core.admin.departments.department.service.DepartmentService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@Controller
@RequestMapping(MappingConstants.API_ADMIN_DEPARTMENT)
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<?> getAllDepartment(FindDepartmentsRequest request) {
        return Helper.createResponseEntity(departmentService.getAllDepartment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailDepartment(@PathVariable String id) {
        return Helper.createResponseEntity(departmentService.getDetailDepartment(id));
    }

    @PostMapping
    public ResponseEntity<?> addDepartment(@RequestBody CreateUpdateDepartmentRequest request) {
        return Helper.createResponseEntity(departmentService.addDepartment(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartment(@RequestBody CreateUpdateDepartmentRequest request, @PathVariable String id) {
        return Helper.createResponseEntity(departmentService.updateDepartment(request, id));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatusDepartment(@PathVariable String id) {
        return Helper.createResponseEntity(departmentService.deleteDepartment(id));
    }

    @PutMapping("/staff/{staffId}")
    public ResponseEntity<?> getDepartmentsManagedByStaff(@PathVariable String staffId) {
        return Helper.createResponseEntity(departmentService.getDepartmentsManagedByStaff(staffId));
    }

    @GetMapping("/synchronize")
    public ResponseEntity<?> synchronize() {
        return Helper.createResponseEntity(departmentService.synchronize());
    }

}