package udpm.hn.server.core.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.common.model.request.CMOptionsFilterRequest;
import udpm.hn.server.core.common.model.request.StaffSearchByRoleRequest;
import udpm.hn.server.core.common.model.request.StaffSearchRequest;
import udpm.hn.server.core.common.service.CommonServiceHelper;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_COMMON)
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommonRestController {

    private final CommonServiceHelper commonServiceHelper;

    @GetMapping("/semester/{id}")
    public ResponseEntity<?> getSemester(@PathVariable("id") String id) {
        return Helper.createResponseEntity(commonServiceHelper.getSemesterInfo(id));
    }

    @GetMapping("/staff/search")
    public ResponseEntity<?> searchStaff(StaffSearchRequest request) {
        return Helper.createResponseEntity(commonServiceHelper.getStaffSearch(request));
    }

    @GetMapping("/staff/role")
    public ResponseEntity<?> searchStaffByRole(StaffSearchByRoleRequest request) {
        return Helper.createResponseEntity(commonServiceHelper.getStaffSearchByRole(request));
    }

    @GetMapping("/department")
    public ResponseEntity<?> getDepartment(CMOptionsFilterRequest request) {
        return Helper.createResponseEntity(commonServiceHelper.getAllDepartmentSubject(request));
    }

    @GetMapping("/facility")
    public ResponseEntity<?> getFacility(CMOptionsFilterRequest request) {
        return Helper.createResponseEntity(commonServiceHelper.getAllFacility(request));
    }

    @GetMapping("/semester")
    public ResponseEntity<?> getSemester(CMOptionsFilterRequest request) {
        return Helper.createResponseEntity(commonServiceHelper.getAllSemester(request));
    }

    @GetMapping("/block")
    public ResponseEntity<?> getBlock(String semesterId) {
        return Helper.createResponseEntity(commonServiceHelper.getBlocks(semesterId));
    }

    @GetMapping("/subject")
    public ResponseEntity<?> getBlock() {
        return Helper.createResponseEntity(commonServiceHelper.getSubject());
    }

}
