package udpm.hn.server.core.superadmin.departments.department.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import udpm.hn.server.core.superadmin.departments.department.model.request.CreateUpdateDepartmentRequest;
import udpm.hn.server.core.superadmin.departments.department.model.request.FindDepartmentsRequest;
import udpm.hn.server.core.superadmin.departments.department.service.SADepartmentService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@Controller
@RequestMapping(MappingConstants.API_SUPER_ADMIN_DEPARTMENT)
@RequiredArgsConstructor
public class SADepartmentController {

    private final SADepartmentService SADepartmentService;

    @GetMapping
    public ResponseEntity<?> getAllDepartment(FindDepartmentsRequest request) {
        return Helper.createResponseEntity(SADepartmentService.getAllDepartment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailDepartment(@PathVariable String id) {
        return Helper.createResponseEntity(SADepartmentService.getDetailDepartment(id));
    }

    @PostMapping
    public ResponseEntity<?> addDepartment(@RequestBody CreateUpdateDepartmentRequest request) {
        return Helper.createResponseEntity(SADepartmentService.addDepartment(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartment(@RequestBody CreateUpdateDepartmentRequest request, @PathVariable String id) {
        return Helper.createResponseEntity(SADepartmentService.updateDepartment(request, id));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatusDepartment(@PathVariable String id) {
        return Helper.createResponseEntity(SADepartmentService.deleteDepartment(id));
    }

    @PutMapping("/staff/{staffId}")
    public ResponseEntity<?> getDepartmentsManagedByStaff(@PathVariable String staffId) {
        return Helper.createResponseEntity(SADepartmentService.getDepartmentsManagedByStaff(staffId));
    }

    @GetMapping("/synchronize")
    public ResponseEntity<?> synchronize() {
        return Helper.createResponseEntity(SADepartmentService.synchronize());
    }

}