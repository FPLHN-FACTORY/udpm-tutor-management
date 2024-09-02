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
import udpm.hn.server.utils.CodeGenerator;
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
                HttpStatus.OK,
                "Get all role successfully");
    }

    @Override
    public ResponseObject<?> getOneRole(String id) {
        return new ResponseObject<>(roleRepository.findById(id),
                HttpStatus.OK,
                "Get one role successfully");
    }

    @Override
    public ResponseObject<?> deleteRole(String id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            //change status in staff_role
            List<StaffRole> staffRoles = staffRoleRepository.findAllByRole_IdAndStatus(id, EntityStatus.ACTIVE);
            staffRoles.stream().forEach(staffRole -> staffRole.setStatus(EntityStatus.INACTIVE));
            staffRoleRepository.saveAll(staffRoles);
            //change status in role
            role.get().setStatus(EntityStatus.INACTIVE);
            roleRepository.save(role.get());
            return new ResponseObject<>(null,
                    HttpStatus.OK,
                    "Delete one role successfully");
        }
        return new ResponseObject<>(null,
                HttpStatus.BAD_REQUEST,
                "Role does not exist");
    }

    @Override
    public ResponseObject<?> getFacilities() {
        return new ResponseObject<>(facilityRepository.getFacilities(), HttpStatus.OK, "Get facilities successfully");
    }

    @Override
    public ResponseObject<?> saveRole(HOSaveRoleRequest roleRequest) {
        Optional<Facility> facility = facilityRepository.findById(roleRequest.getIdFacility());
        if (facility.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cơ sở không tồn tại");
        }

        // Chuyển role name chuỗi thành chữ hoa
        String upperCaseString = roleRequest.getRoleName().toUpperCase();

        // Loại bỏ dấu
        String normalizedString = Normalizer.normalize(upperCaseString, Normalizer.Form.NFD);
        String withoutAccentString = normalizedString.replaceAll("\\p{M}", "");

        // Thay thế tất cả khoảng trắng liên tiếp bằng dấu gạch dưới
        String roleCode = withoutAccentString.replaceAll("\\s+", "_");

        //create role
        if (roleRequest.getRoleId() == null || roleRequest.getRoleId().isEmpty()) {
            Role role = new Role();
            role.setId(CodeGenerator.generateRandomCode());
            role.setCode(roleCode.trim());
            role.setName(roleRequest.getRoleName());
            role.setFacility(facility.isPresent() ? facility.get() : null);
            role.setStatus(EntityStatus.ACTIVE);

            List<Role> roles = roleRepository.findAllByCodeAndFacility_Id(roleCode.trim(), roleRequest.getIdFacility());

            if (roles.isEmpty()) {
                roleRepository.save(role);
                return new ResponseObject<>(null, HttpStatus.CREATED, "Thêm chức vụ thành công");
            } else {
                if (roleRepository.findAllByCodeAndFacility_IdAndStatus(roleCode.trim(), roleRequest.getIdFacility(), EntityStatus.ACTIVE).isEmpty()) {
                    Role r = roleRepository.findAllByCodeAndFacility_IdAndStatus(roleCode.trim(), roleRequest.getIdFacility(), EntityStatus.INACTIVE).get(0);
                    r.setStatus(EntityStatus.ACTIVE);
                    roleRepository.save(r);
                    return new ResponseObject<>(null, HttpStatus.CREATED, "Thêm chức vụ thành công");
                }
                return new ResponseObject<>(null, HttpStatus.CONFLICT, "Chức vụ đã tồn tại");
            }
        }
        //update role
        else {
            Optional<Role> role = roleRepository.findById(roleRequest.getRoleId());
            if (role.isEmpty()) {
                return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Không tìm thấy chức vụ");
            } else {
                role.get().setCode(roleCode.trim());
                role.get().setName(roleRequest.getRoleName().trim());
                role.get().setFacility(facility.isPresent() ? facility.get() : null);
                List<Role> optionalRole = roleRepository.findAllByCodeAndFacility_Id(roleCode.trim(), roleRequest.getIdFacility());
                if (optionalRole.isEmpty()) {
                    roleRepository.save(role.get());
                    return new ResponseObject<>(null, HttpStatus.OK, "Update chức vụ thành công");
                } else {
                    for (Role role1 : optionalRole) {
                        if (role1.getId().equals(role.get().getId())) {
                            roleRepository.save(role.get());
                            return new ResponseObject<>(null, HttpStatus.OK, "Update chức vụ thành công");
                        }
                    }
                    return new ResponseObject<>(null, HttpStatus.CONFLICT, "Chức vụ đã tồn tại");
                }
            }
        }
    }
}
