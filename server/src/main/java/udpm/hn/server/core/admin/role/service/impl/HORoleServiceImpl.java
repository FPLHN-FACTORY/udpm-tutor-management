package udpm.hn.server.core.admin.role.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.admin.role.model.request.HORoleRequest;
import udpm.hn.server.core.admin.role.model.request.HOSaveRoleRequest;
import udpm.hn.server.core.admin.role.repository.HORoleFacilityRepository;
import udpm.hn.server.core.admin.role.repository.HORoleRepository;
import udpm.hn.server.core.admin.role.repository.HORoleStaffRepository;
import udpm.hn.server.core.admin.role.service.HORoleService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.entity.Role;
import udpm.hn.server.entity.StaffRole;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.utils.Helper;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HORoleServiceImpl implements HORoleService {

    private final HORoleRepository roleRepository;

    private final HORoleFacilityRepository facilityRepository;

    private final HORoleStaffRepository staffRoleRepository;

    @Override
    public ResponseObject<?> getAllRole(HORoleRequest roleRequest) {
        Pageable page = Helper.createPageable(roleRequest);
        return new ResponseObject<>(PageableObject.of(roleRepository.getAllRole(page, roleRequest)),
                HttpStatus.OK, "Get all role successfully");
    }

    @Override
    public ResponseObject<?> getOneRole(String id) {
        return new ResponseObject<>(roleRepository.findById(id),
                HttpStatus.OK, "Get one role successfully");
    }

    @Override
    public ResponseObject<?> deleteRole(String id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            List<StaffRole> staffRoles = staffRoleRepository.findAllByRole_IdAndStatus(id, EntityStatus.ACTIVE);
            staffRoles.forEach(staffRole -> staffRole.setStatus(EntityStatus.INACTIVE));
            staffRoleRepository.saveAll(staffRoles);

            role.get().setStatus(EntityStatus.INACTIVE);
            roleRepository.save(role.get());
            return new ResponseObject<>(null, HttpStatus.OK, "Delete one role successfully");
        }
        return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Role does not exist");
    }

    @Override
    public ResponseObject<?> saveRole(HOSaveRoleRequest roleRequest) {
        Optional<Facility> facility = facilityRepository.findById(roleRequest.getIdFacility());
        if (facility.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cơ sở không tồn tại");
        }

        String roleCode = Normalizer.normalize(roleRequest.getRoleName().toUpperCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").replaceAll("\\s+", "_").trim();

        if (roleRequest.getRoleId() == null || roleRequest.getRoleId().isEmpty()) {
            return createRole(roleRequest, facility, roleCode);
        } else {
            return updateRole(roleRequest, facility, roleCode);
        }
    }

    private ResponseObject<?> createRole(HOSaveRoleRequest roleRequest, Optional<Facility> facility, String roleCode) {
        Role role = new Role();
        role.setCode(roleCode);
        role.setName(roleRequest.getRoleName());
        role.setFacility(facility.orElse(null));
        role.setStatus(EntityStatus.ACTIVE);

        List<Role> roles = roleRepository.findAllByCodeAndFacility_Id(roleCode, roleRequest.getIdFacility());
        if (roles.isEmpty()) {
            roleRepository.save(role);
            return new ResponseObject<>(null, HttpStatus.CREATED, "Thêm chức vụ thành công");
        } else {
            return handleExistingRole(roleCode, roleRequest, roles);
        }
    }

    private ResponseObject<?> handleExistingRole(String roleCode, HOSaveRoleRequest roleRequest, List<Role> roles) {
        if (roleRepository.findAllByCodeAndFacility_IdAndStatus(roleCode, roleRequest.getIdFacility(), EntityStatus.ACTIVE).isEmpty()) {
            Role r = roleRepository.findAllByCodeAndFacility_IdAndStatus(roleCode, roleRequest.getIdFacility(), EntityStatus.INACTIVE).get(0);
            r.setStatus(EntityStatus.ACTIVE);
            roleRepository.save(r);
            return new ResponseObject<>(null, HttpStatus.CREATED, "Thêm chức vụ thành công");
        }
        return new ResponseObject<>(null, HttpStatus.CONFLICT, "Chức vụ đã tồn tại");
    }

    private ResponseObject<?> updateRole(HOSaveRoleRequest roleRequest, Optional<Facility> facility, String roleCode) {
        Optional<Role> role = roleRepository.findById(roleRequest.getRoleId());
        if (role.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Không tìm thấy chức vụ");
        } else {
            role.get().setCode(roleCode);
            role.get().setName(roleRequest.getRoleName().trim());
            role.get().setFacility(facility.orElse(null));

            List<Role> optionalRole = roleRepository.findAllByCodeAndFacility_Id(roleCode, roleRequest.getIdFacility());
            if (optionalRole.isEmpty() || optionalRole.stream().anyMatch(r -> r.getId().equals(role.get().getId()))) {
                roleRepository.save(role.get());
                return new ResponseObject<>(null, HttpStatus.OK, "Update chức vụ thành công");
            }
            return new ResponseObject<>(null, HttpStatus.CONFLICT, "Chức vụ đã tồn tại");
        }
    }

}