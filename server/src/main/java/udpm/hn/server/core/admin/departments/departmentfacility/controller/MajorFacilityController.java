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
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.CreateMajorFacilityRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.MajorFacilityRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.UpdateMajorFacilityRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.service.MajorFacilityService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@Controller
@RequestMapping(MappingConstants.API_ADMIN_MAJOR_FACILITY)
@RequiredArgsConstructor
public class MajorFacilityController {

    private final MajorFacilityService majorFacilityService;

    @GetMapping
    public ResponseEntity<?> getMajorFacilities(MajorFacilityRequest request) {
        return Helper.createResponseEntity(majorFacilityService.getAllMajorFacilities(request));
    }

    @PostMapping
    public ResponseEntity<?> createMajorFacility(@RequestBody CreateMajorFacilityRequest request) {
        return Helper.createResponseEntity(majorFacilityService.createMajorFacility(request));
    }

    @PutMapping("/{majorFacilityId}")
    public ResponseEntity<?> updateMajorFacility(@PathVariable String majorFacilityId, @RequestBody UpdateMajorFacilityRequest request) {
        return Helper.createResponseEntity(majorFacilityService.updateMajorFacility(majorFacilityId, request));
    }

    @GetMapping("/{majorFacilityId}")
    public ResponseEntity<?> getMajorFacilityById(@PathVariable String majorFacilityId) {
        return Helper.createResponseEntity(majorFacilityService.getMajorFacilityById(majorFacilityId));
    }

    @GetMapping("/major/{departmentId}")
    public ResponseEntity<?> getMajorFacilities(@PathVariable String departmentId) {
        return Helper.createResponseEntity(majorFacilityService.getAllMajors(departmentId));
    }

}