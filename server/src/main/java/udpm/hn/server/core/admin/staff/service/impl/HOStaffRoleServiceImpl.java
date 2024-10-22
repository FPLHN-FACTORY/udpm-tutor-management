package udpm.hn.server.core.admin.staff.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import udpm.hn.server.core.admin.staff.model.request.HOStaffRoleChangePermissionRequest;
import udpm.hn.server.core.admin.staff.model.request.HOStaffRoleRequest;
import udpm.hn.server.core.admin.staff.repository.HOStaffFacilityRepository;
import udpm.hn.server.core.admin.staff.repository.HOStaffRepository;
import udpm.hn.server.core.admin.staff.repository.HOStaffRoleRepository;
import udpm.hn.server.core.admin.staff.service.HOStaffRoleService;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.superadmin.operation.model.request.OperationLogsRequest;
import udpm.hn.server.core.superadmin.operation.service.OperationLogsService;
import udpm.hn.server.entity.Role;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.StaffRole;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.FunctionLogType;
import udpm.hn.server.repository.RoleRepository;
import udpm.hn.server.utils.CodeGenerator;
import udpm.hn.server.utils.Helper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HOStaffRoleServiceImpl implements HOStaffRoleService {

    private final HOStaffRoleRepository staffRoleRepository;

    private final HOStaffRepository staffRepository;

    private final RoleRepository roleRepository;

    private final HOStaffFacilityRepository facilityRepository;

    private final OperationLogsService logsService;

    @Override
    public ResponseObject<?> getAllRole(String staffId) {
        return new ResponseObject<>(staffRoleRepository.getRolesByStaffId(staffId),
                HttpStatus.OK,
                "Get roles by staff id successfully!");
    }

    @Override
    public ResponseObject<?> getRolesChecked(HOStaffRoleRequest roleRequest) {
        Pageable page = Helper.createPageable(roleRequest, "createdDate");
        return new ResponseObject<>(staffRoleRepository.getRolesChecked(page, roleRequest), HttpStatus.OK, "get roles checked successfully");
    }

    @Override
    public ResponseObject<?> updateStaffRole(HOStaffRoleChangePermissionRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.UPDATE);
        log.setStatus(true);
        try {
            Optional<Role> role = roleRepository.findById(request.getIdRole());
            Optional<Staff> staff = staffRepository.findById(request.getIdStaff());
            if (role.isEmpty() || staff.isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("Staff or role not found");
                return new ResponseObject<>(null, HttpStatus.NOT_FOUND, log.getErrorMessage());
            }
            List<StaffRole> staffRoles = staffRoleRepository.findAllByRole_IdAndStaff_Id(request.getIdRole(), request.getIdStaff());
            if (staffRoles.isEmpty()) {
                StaffRole staffRole = new StaffRole();
                staffRole.setRole(role.get());
                staffRole.setStaff(staff.get());
                staffRole.setId(CodeGenerator.generateRandomCode());
                staffRole.setStatus(EntityStatus.ACTIVE);
                staffRoleRepository.save(staffRole);
            } else {
                staffRoles.get(0).setStatus(staffRoles.get(0).getStatus().equals(EntityStatus.INACTIVE) ? EntityStatus.ACTIVE : EntityStatus.INACTIVE);
                staffRoleRepository.save(staffRoles.get(0));
            }
            log.setSuccessMessage("change permission successfully");
            return new ResponseObject<>(null, HttpStatus.OK, log.getSuccessMessage());
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Có lỗi say ra khi change permission!");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> getFacilities() {
        return new ResponseObject<>(facilityRepository.getFacilities(), HttpStatus.OK, "Get facilities successfully");
    }

    @Override
    public ResponseObject<?> getFacilitiesSelect(String idStaff) {
        return new ResponseObject<>(facilityRepository.getFacilitiesSelect(idStaff), HttpStatus.OK, "Get facilities successfully");
    }

}
