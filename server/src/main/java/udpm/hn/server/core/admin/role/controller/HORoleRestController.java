package udpm.hn.server.core.admin.role.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.admin.role.model.request.HORoleRequest;
import udpm.hn.server.core.admin.role.model.request.HOSaveRoleRequest;
import udpm.hn.server.core.admin.role.service.HORoleService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_HEAD_OFFICE_ROLE)
@RequiredArgsConstructor
@CrossOrigin("*")
public class HORoleRestController {

    private final HORoleService roleService;

    @GetMapping
    public ResponseEntity<?> getAll(HORoleRequest hoRoleRequest) {
        return Helper.createResponseEntity(roleService.getAllRole(hoRoleRequest));
    }

    @GetMapping("/facilities")
    public ResponseEntity<?> getFacilities() {
        return Helper.createResponseEntity(roleService.getFacilities());
    }

    @PostMapping
    public ResponseEntity<?> addRole(@Valid @RequestBody HOSaveRoleRequest hoRoleRequest) {
        return Helper.createResponseEntity(roleService.saveRole(hoRoleRequest));
    }

    @PutMapping
    public ResponseEntity<?> updateRole(@Valid @RequestBody HOSaveRoleRequest hoRoleRequest) {
        return Helper.createResponseEntity(roleService.saveRole(hoRoleRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneRole(@PathVariable(value = "id") String id) {
        return Helper.createResponseEntity(roleService.getOneRole(id));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRole(@RequestParam(value = "id") String id) {
        return Helper.createResponseEntity(roleService.deleteRole(id));
    }
}
