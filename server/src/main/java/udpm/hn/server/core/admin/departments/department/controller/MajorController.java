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
import udpm.hn.server.core.admin.departments.department.model.request.CreateOrUpdateMajorRequest;
import udpm.hn.server.core.admin.departments.department.model.request.FindMajorRequest;
import udpm.hn.server.core.admin.departments.department.service.MajorService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@Controller
@RequestMapping(MappingConstants.REDIRECT_ADMIN_MAJOR)
@RequiredArgsConstructor
public class MajorController {

    private final MajorService majorService;

    @GetMapping("/get-all-major/{departmentId}")
    public ResponseEntity<?> getAllMajor(@PathVariable String departmentId, FindMajorRequest request) {
        return Helper.createResponseEntity(majorService.getAllMajor(departmentId, request));
    }

    @PostMapping("/add-major")
    public ResponseEntity<?> addCoSo(@RequestBody CreateOrUpdateMajorRequest request) {
        return Helper.createResponseEntity(majorService.addMajor(request));
    }

    @PutMapping("/update-major/{id}")
    public ResponseEntity<?> updateCoSo(@RequestBody CreateOrUpdateMajorRequest request, @PathVariable String id) {
        return Helper.createResponseEntity(majorService.updateMajor(request, id));
    }

    @PutMapping("/delete-major/{id}")
    public ResponseEntity<?> deleteCoSo(@PathVariable String id) {
        return Helper.createResponseEntity(majorService.deleteMajor(id));
    }

}