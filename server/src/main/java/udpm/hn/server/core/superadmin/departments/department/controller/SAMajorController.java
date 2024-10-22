package udpm.hn.server.core.superadmin.departments.department.controller;

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
import udpm.hn.server.core.superadmin.departments.department.model.request.CreateOrUpdateMajorRequest;
import udpm.hn.server.core.superadmin.departments.department.model.request.FindMajorRequest;
import udpm.hn.server.core.superadmin.departments.department.service.SAMajorService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@Controller
@RequestMapping(MappingConstants.API_SUPER_ADMIN_MAJOR)
@RequiredArgsConstructor
public class SAMajorController {

    private final SAMajorService SAMajorService;

    @GetMapping("/{departmentId}")
    public ResponseEntity<?> getAllMajor(@PathVariable String departmentId, FindMajorRequest request) {
        return Helper.createResponseEntity(SAMajorService.getAllMajor(departmentId, request));
    }

    @PostMapping
    public ResponseEntity<?> addMajor(@RequestBody CreateOrUpdateMajorRequest request) {
        return Helper.createResponseEntity(SAMajorService.addMajor(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMajor(@RequestBody CreateOrUpdateMajorRequest request, @PathVariable String id) {
        return Helper.createResponseEntity(SAMajorService.updateMajor(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMajor(@PathVariable String id) {
        return Helper.createResponseEntity(SAMajorService.deleteMajor(id));
    }

}