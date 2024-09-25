package udpm.hn.server.core.admin.departments.departmentfacility.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.CreateOrUpdateDepartmentFacilityRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.FindFacilityDetailRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.service.DepartmentFacilityService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@Controller
@RequestMapping(MappingConstants.API_ADMIN_DEPARTMENT_FACILITY)
@RequiredArgsConstructor
public class DepartmentFacilityController {

    private final DepartmentFacilityService departmentFacilityService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllDepartmentFacility(@PathVariable String id, FindFacilityDetailRequest request) {
        return Helper.createResponseEntity(departmentFacilityService.getAllDepartmentFacility(id, request));
    }

    @PostMapping("/add-department-facility")
    public ResponseEntity<?> addDepartmentFacility(@RequestBody CreateOrUpdateDepartmentFacilityRequest request) {
        return Helper.createResponseEntity(departmentFacilityService.addDepartmentFacility(request));
    }

    @PutMapping("/update-department-facility/{id}")
    public ResponseEntity<?> updateDepartmentFacility(@PathVariable String id, @RequestBody CreateOrUpdateDepartmentFacilityRequest request) {
        return Helper.createResponseEntity(departmentFacilityService.updateDepartmentFacility(request, id));
    }

    @PutMapping("/delete-department-facility/{id}")
    public ResponseEntity<?> deleteDepartmentFacility(@PathVariable String id) {
        return Helper.createResponseEntity(departmentFacilityService.deleteDepartmentFacility(id));
    }

    @GetMapping("/get-list-facility")
    public ResponseEntity<?> getListFacilityName() {
        return Helper.createResponseEntity(departmentFacilityService.getListFacility());
    }

    @GetMapping("/get-list-head-of-department")
    public ResponseEntity<?> getListHeadOfDepartment() {
        return Helper.createResponseEntity(departmentFacilityService.getListStaff());
    }

    @GetMapping("/get-department-name/{departmentId}")
    public ResponseEntity<?> getDepartmentName(@PathVariable String departmentId) {
        return Helper.createResponseEntity(departmentFacilityService.getDepartmentName(departmentId));
    }

    @GetMapping("/synchronize")
    public ResponseEntity<?> synchronize() {
        return Helper.createResponseEntity(departmentFacilityService.synchronize());
    }

}
