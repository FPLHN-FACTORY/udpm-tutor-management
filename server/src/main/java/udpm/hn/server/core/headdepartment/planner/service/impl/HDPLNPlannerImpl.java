package udpm.hn.server.core.headdepartment.planner.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headdepartment.planner.model.request.HDPLNAssignedPlannerRequest;
import udpm.hn.server.core.headdepartment.planner.model.request.HDPLNPlannerListRequest;
import udpm.hn.server.core.headdepartment.planner.repository.HDPLNStaffsRepository;
import udpm.hn.server.core.headdepartment.planner.service.HDPLNPlannerService;
import udpm.hn.server.entity.Role;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.StaffRole;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.RoleRepository;
import udpm.hn.server.repository.StaffRoleRepository;
import udpm.hn.server.utils.Helper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class HDPLNPlannerImpl implements HDPLNPlannerService {

    private final HDPLNStaffsRepository HDPLNStaffsRepository;

    private final StaffRoleRepository staffRoleRepository;

    private final RoleRepository roleRepository;

    @Override
    public ResponseObject<?> getAllPlannerManager(HDPLNPlannerListRequest request) {
        request.setHeadSubjectRoleCode(udpm.hn.server.infrastructure.constant.Role.NGUOI_LAP_KE_HOACH.name());
        return new ResponseObject<>(
                PageableObject.of(
                        HDPLNStaffsRepository.getAllPlannerManager(
                                Helper.createPageable(request),
                                request
                        )
                ),
                HttpStatus.OK,
                "Lấy danh sách trưởng bộ môn thành công"
        );
    }

    @Override
    public ResponseObject<?> isAssignedPlanner(String id, HDPLNAssignedPlannerRequest request) {
        String idStaffRolePlanner = HDPLNStaffsRepository.findSubjectPlannerIdByRole(request);
        Staff staff = null;
        if (idStaffRolePlanner != null) {
            staff = HDPLNStaffsRepository.findById(idStaffRolePlanner).orElse(null);
            if (staff != null && staff.getId() != null) {
                // Lấy danh sách StaffRole của nhân viên
                List<StaffRole> staffRoles = staffRoleRepository.findByStaffIdV2(staff.getId());

                // Lặp qua danh sách staffRoles để xóa các role có code là "NGUOI_LAP_KE_HOACH"
                for (StaffRole staffRole : staffRoles) {
                    if (staffRole.getRole().getCode().equals(udpm.hn.server.infrastructure.constant.Role.NGUOI_LAP_KE_HOACH.name())) {
                        staffRoleRepository.delete(staffRole);
                    }
                }
            }
        }

        Staff staffAssigned = HDPLNStaffsRepository.findById(id).orElse(null);
        if (staffAssigned == null) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Nhân viên không tồn tại");
        }
        Optional<Role> role = roleRepository.findByCode(udpm.hn.server.infrastructure.constant.Role.NGUOI_LAP_KE_HOACH.name());
        if (role.isPresent()) {
            StaffRole staffRole = new StaffRole();
            staffRole.setRole(role.get());
            staffRole.setStaff(staffAssigned);
            staffRole.setCreatedDate(System.currentTimeMillis());
            staffRole.setStatus(EntityStatus.ACTIVE);

            staffRoleRepository.save(staffRole);
        }

        return new ResponseObject<>(
                null,
                HttpStatus.OK,
                "Phân công thành công!"
        );
    }

    @Override
    public ResponseObject<?> unAssignPlanner(String id) {
        Staff staff = HDPLNStaffsRepository.findById(id).orElse(null);
        if (staff == null) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Nhân viên không tồn tại");
        }

        List<StaffRole> staffRoles = staffRoleRepository.findByStaffIdV2(staff.getId());
        boolean roleRemoved = false;
        for (StaffRole staffRole : staffRoles) {
            if (staffRole.getRole().getCode().equals(udpm.hn.server.infrastructure.constant.Role.NGUOI_LAP_KE_HOACH.name())) {
                staffRoleRepository.delete(staffRole);
                roleRemoved = true;
            }
        }

        if (!roleRemoved) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Nhân viên không có vai trò NGUOI_LAP_KE_HOACH");
        }

        return new ResponseObject<>(null, HttpStatus.OK, "Hủy phân công thành công!");
    }

}
