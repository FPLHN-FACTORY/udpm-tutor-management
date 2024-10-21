package udpm.hn.server.core.superadmin.departments.departmentfacility.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import udpm.hn.server.core.superadmin.departments.departmentfacility.model.request.CreateOrUpdateDepartmentFacilityRequest;
import udpm.hn.server.core.superadmin.departments.departmentfacility.model.request.FindFacilityDetailRequest;
import udpm.hn.server.core.superadmin.departments.departmentfacility.service.SADepartmentFacilityService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@Controller
@RequestMapping(MappingConstants.API_SUPER_ADMIN_DEPARTMENT_FACILITY)
@RequiredArgsConstructor
public class SADepartmentFacilityController {

    private final SADepartmentFacilityService SADepartmentFacilityService;

    @GetMapping("{id}")
    public ResponseEntity<?> getAllDepartmentFacility(@PathVariable String id, FindFacilityDetailRequest request) {
        return Helper.createResponseEntity(SADepartmentFacilityService.getAllDepartmentFacility(id, request));
    }

    @PostMapping
    public ResponseEntity<?> addDepartmentFacility(@RequestBody CreateOrUpdateDepartmentFacilityRequest request) {
        return Helper.createResponseEntity(SADepartmentFacilityService.addDepartmentFacility(request));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateDepartmentFacility(@PathVariable String id, @RequestBody CreateOrUpdateDepartmentFacilityRequest request) {
        return Helper.createResponseEntity(SADepartmentFacilityService.updateDepartmentFacility(request, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteDepartmentFacility(@PathVariable String id) {
        return Helper.createResponseEntity(SADepartmentFacilityService.deleteDepartmentFacility(id));
    }

    @GetMapping("/get-list-facility")
    public ResponseEntity<?> getListFacilityName() {
        return Helper.createResponseEntity(SADepartmentFacilityService.getListFacility());
    }

    @GetMapping("/get-list-head-of-department")
    public ResponseEntity<?> getListHeadOfDepartment() {
        return Helper.createResponseEntity(SADepartmentFacilityService.getListStaff());
    }

    @GetMapping("/get-department-name/{departmentId}")
    public ResponseEntity<?> getDepartmentName(@PathVariable String departmentId) {
        return Helper.createResponseEntity(SADepartmentFacilityService.getDepartmentName(departmentId));
    }

}
