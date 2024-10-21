package udpm.hn.server.core.superadmin.departments.departmentfacility.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import udpm.hn.server.core.superadmin.departments.departmentfacility.model.request.CreateMajorFacilityRequest;
import udpm.hn.server.core.superadmin.departments.departmentfacility.model.request.MajorFacilityRequest;
import udpm.hn.server.core.superadmin.departments.departmentfacility.model.request.UpdateMajorFacilityRequest;
import udpm.hn.server.core.superadmin.departments.departmentfacility.service.SAMajorFacilityService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@Controller
@RequestMapping(MappingConstants.API_SUPER_ADMIN_MAJOR_FACILITY)
@RequiredArgsConstructor
public class SAMajorFacilityController {

    private final SAMajorFacilityService SAMajorFacilityService;

    @GetMapping
    public ResponseEntity<?> getMajorFacilities(MajorFacilityRequest request) {
        return Helper.createResponseEntity(SAMajorFacilityService.getAllMajorFacilities(request));
    }

    @PostMapping
    public ResponseEntity<?> createMajorFacility(@RequestBody CreateMajorFacilityRequest request) {
        return Helper.createResponseEntity(SAMajorFacilityService.createMajorFacility(request));
    }

    @PutMapping("/{majorFacilityId}")
    public ResponseEntity<?> updateMajorFacility(@PathVariable String majorFacilityId, @RequestBody UpdateMajorFacilityRequest request) {
        return Helper.createResponseEntity(SAMajorFacilityService.updateMajorFacility(majorFacilityId, request));
    }

    @GetMapping("/{majorFacilityId}")
    public ResponseEntity<?> getMajorFacilityById(@PathVariable String majorFacilityId) {
        return Helper.createResponseEntity(SAMajorFacilityService.getMajorFacilityById(majorFacilityId));
    }

    @GetMapping("/major/{departmentId}")
    public ResponseEntity<?> getMajorFacilities(@PathVariable String departmentId) {
        return Helper.createResponseEntity(SAMajorFacilityService.getAllMajors(departmentId));
    }

}