package udpm.hn.server.core.admin.staff.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import udpm.hn.server.core.admin.staff.model.request.HOSaveStaffRequest;
import udpm.hn.server.core.admin.staff.model.request.HOStaffRequest;
import udpm.hn.server.core.admin.staff.model.response.HOStaffDepartmentFacilityResponse;
import udpm.hn.server.core.admin.staff.model.response.HOStaffResonpse;
import udpm.hn.server.core.admin.staff.repository.HOStaffDepartmentFacilityRepository;
import udpm.hn.server.core.admin.staff.repository.HOStaffFacilityRepository;
import udpm.hn.server.core.admin.staff.repository.HOStaffMajorFacilityRepository;
import udpm.hn.server.core.admin.staff.repository.HOStaffMajorFacilityStaffRepository;
import udpm.hn.server.core.admin.staff.repository.HOStaffRepository;
import udpm.hn.server.core.admin.staff.repository.HOStaffRoleRepository;
import udpm.hn.server.core.admin.staff.service.HOStaffService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.superadmin.operation.model.request.OperationLogsRequest;
import udpm.hn.server.core.superadmin.operation.service.OperationLogsService;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.entity.MajorFacility;
import udpm.hn.server.entity.Role;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.StaffMajorFacility;
import udpm.hn.server.entity.StaffRole;
import udpm.hn.server.infrastructure.connection.IdentityConnection;
import udpm.hn.server.infrastructure.connection.response.StaffResponse;
import udpm.hn.server.infrastructure.connection.response.StaffRoleResponse;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.FunctionLogType;
import udpm.hn.server.repository.RoleRepository;
import udpm.hn.server.utils.Helper;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HOStaffServiceImpl implements HOStaffService {

    private final RoleRepository roleRepository;

    private final HOStaffRepository staffRepo;

    private final HOStaffDepartmentFacilityRepository departmentFacilityRepo;

    private final HOStaffMajorFacilityRepository majorFacilityRepository;

    private final HOStaffMajorFacilityStaffRepository majorFacilityStaffRepository;

    private final HOStaffRoleRepository staffRoleRepo;

    private final IdentityConnection identityConnection;

    private final HOStaffFacilityRepository facilityRepository;

    private final OperationLogsService logsService;

    @Override
    public ResponseObject<?> getStaffByRole(HOStaffRequest hoRoleStaffRequest) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(hoRoleStaffRequest);
        log.setTypeFunction(FunctionLogType.SEARCH);
        log.setStatus(true);
        try {
            Pageable page = Helper.createPageable(hoRoleStaffRequest, "createdDate");
            Page<HOStaffResonpse> staffResponse = staffRepo.getStaffs(page, hoRoleStaffRequest);
            log.setResponse(staffResponse.getContent());
            return new ResponseObject<>(
                    PageableObject.of(staffResponse),
                    HttpStatus.OK,
                    "get staffs successfully");
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình lấy danh sách nhân viên");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> getDepartmentFacility() {
        return new ResponseObject<>(departmentFacilityRepo.getDepartmentFacilities(), HttpStatus.OK, "get departments facilities successfully");
    }

    @Override
    public ResponseObject<?> detailStaff(String staffId) {
        return new ResponseObject<>(staffRepo.getStaff(staffId), HttpStatus.OK, "get one staff successfully");
    }

    @Override
    public ResponseObject<?> createStaff(HOSaveStaffRequest staffRequest) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(staffRequest);
        log.setTypeFunction(FunctionLogType.CREATE);
        log.setStatus(true);
        try {
            Staff staff = new Staff();

            staff.setName(staffRequest.getName());

            Optional<Staff> staffList = staffRepo.findByStaffCode(staffRequest.getStaffCode());

            if (!staffList.isEmpty() && staffList.get().getStatus() == EntityStatus.ACTIVE) {
                log.setStatus(false);
                log.setErrorMessage("Mã nhân viên đã tồn tại");
                return new ResponseObject<>(null, HttpStatus.CONFLICT, log.getErrorMessage());
            }

            staff.setStatus(EntityStatus.ACTIVE);
            staff.setStaffCode(staffRequest.getStaffCode());
            staff.setEmailFe(staffRequest.getEmailFe());
            staff.setEmailFpt(staffRequest.getEmailFpt());
            staffRepo.save(staff);
            log.setSuccessMessage("Thêm nhân viên thành công");
            return new ResponseObject<>(null, HttpStatus.CREATED, log.getSuccessMessage());
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình lấy thêm nhân viên");
        } finally {
            logsService.createOperationLog(log);
        }

    }

    @Override
    public ResponseObject<?> updateStaff(HOSaveStaffRequest staffRequest) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(staffRequest);
        log.setTypeFunction(FunctionLogType.UPDATE);
        log.setStatus(true);
        try {
            Optional<Staff> staffOptional = staffRepo.findById(staffRequest.getId());

            if (staffOptional.isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("Không tìm thấy nhân viên");
                return new ResponseObject<>(null, HttpStatus.NOT_FOUND, log.getErrorMessage());
            }

            Staff staff = staffOptional.get();
            staff.setId(staffRequest.getId());
            staff.setName(staffRequest.getName());
            Optional<Staff> checkStaff = staffRepo.findByStaffCode(staffRequest.getStaffCode());

            if (!checkStaff.isEmpty() &&
                    !checkStaff.get().getId().equalsIgnoreCase(staffRequest.getStaffCode()) &&
                    checkStaff.get().getStatus() == EntityStatus.ACTIVE) {
                if (!staff.getId().equals(checkStaff.get().getId())) {
                    log.setStatus(false);
                    log.setErrorMessage("Mã nhân viên đã tồn tại");
                    return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, log.getErrorMessage());
                }
            }

            staff.setStaffCode(staffRequest.getStaffCode());
            staff.setEmailFe(staffRequest.getEmailFe());
            staff.setEmailFpt(staffRequest.getEmailFpt());
            staffRepo.save(staff);
            log.setSuccessMessage("Sửa nhân viên thành công");
            return new ResponseObject<>(null, HttpStatus.OK, log.getSuccessMessage());
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình lấy cập nhật nhân viên");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> deleteStaff(String idStaff) {
        Optional<Staff> staffOptional = staffRepo.findById(idStaff);
        if (staffOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "staff not found");
        }
        //change status in staff_role
        List<StaffRole> staffRoles = staffRoleRepo.findAllByStaff_IdAndStatus(idStaff, EntityStatus.ACTIVE);
        staffRoles.forEach(staffRole -> staffRole.setStatus(EntityStatus.INACTIVE));
        staffRoleRepo.saveAll(staffRoles);
        //change status in staff
        staffOptional.get().setStatus(EntityStatus.INACTIVE);
        staffRepo.save(staffOptional.get());
        return new ResponseObject<>(null, HttpStatus.OK, "delete staff successfully");
    }

    @Override
    @Transactional
    public ResponseObject<?> synchronize(String campusCode) {
        try {
            List<StaffResponse> staffData = identityConnection.getStaffs(campusCode);
            List<Staff> existingStaffs = staffRepo.findAll();

            if (existingStaffs.isEmpty()) {
                // Synchronize all staff if none exist
                staffData.forEach(staffResponse -> syncStaff(null, staffResponse)); // Pass null for new staff
            } else {
                // Synchronize each staff with existing records
                staffData.forEach(staffResponse -> {
                    existingStaffs.forEach(staff -> syncStaff(staff, staffResponse));
                });
            }

            return ResponseObject.successForward(null, "Đồng bộ nhân viên thành công!");
        } catch (Exception e) {
            log.error("Lỗi khi đồng bộ nhân viên : {}", e.getMessage());
            return ResponseObject.errorForward(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void syncStaff(Staff staff, StaffResponse staffResponse) {
        StaffResponse.MajorDepartmentCampus majorDepartmentCampus = staffResponse.getMajorDepartmentCampus();
        String departmentCode = majorDepartmentCampus.getDepartmentCampus().getDepartmentCode();
        String campusCode = majorDepartmentCampus.getCampus().getCampusCode();
        String majorCode = majorDepartmentCampus.getMajorCampus().getMajorCode();
        Facility facility = facilityRepository.findByCode(campusCode).orElse(null);

        Optional<HOStaffDepartmentFacilityResponse> departmentFacilities = departmentFacilityRepo.getDepartmentFacilities(departmentCode, campusCode);
        if (departmentFacilities.isEmpty()) {
            throw new RuntimeException("Chưa có đồng bộ bộ môn theo cơ sở");
        }

        List<MajorFacility> majorFacilities = majorFacilityRepository.findAllByDepartmentFacility_IdAndMajor_Code(
                departmentFacilities.get().getDepartmentFacilityId(), majorCode);

        if (majorFacilities.isEmpty()) {
            throw new RuntimeException("Chưa đồng bộ chuyên ngành theo cơ sở");
        }

        Staff postStaff = (staff == null) ? new Staff() : staffRepo.findByStaffCode(staffResponse.getStaffDetail().getStaffCode()).orElseGet(Staff::new);

        StaffResponse.StaffDetail staffDetail = staffResponse.getStaffDetail();
        postStaff.setStaffCode(staffDetail.getStaffCode());
        postStaff.setName(staffDetail.getFullName());
        postStaff.setPicture(staffDetail.getAvatar());
        postStaff.setEmailFe(staffDetail.getEmailFe());
        postStaff.setEmailFpt(staffDetail.getEmailFpt());
        postStaff.setStatus(EntityStatus.ACTIVE);
        postStaff.setStaffIdentityId(staffResponse.getStaffDetail().getId());

        Staff savedStaff = staffRepo.save(postStaff);

        StaffMajorFacility staffMajorFacility = (staff == null) ? new StaffMajorFacility() : majorFacilityStaffRepository.findByStaff_Id(savedStaff.getId()).orElseGet(StaffMajorFacility::new);
        staffMajorFacility.setStaff(savedStaff);
        staffMajorFacility.setMajorFacility(majorFacilities.get(0));
        staffMajorFacility.setStatus(EntityStatus.ACTIVE);
        majorFacilityStaffRepository.save(staffMajorFacility);

        List<StaffRoleResponse> staffRoleResponses = identityConnection.getRoleStaffs(campusCode, savedStaff.getStaffCode());
        if (!staffRoleResponses.isEmpty()) {
            List<StaffRole> existingRoles = staffRoleRepo.findAllByStaff_IdAndStatus(savedStaff.getId(), EntityStatus.ACTIVE);

            staffRoleResponses.forEach(staffRoleResponse -> {
                boolean exists = existingRoles.stream()
                        .anyMatch(role -> role.getRole().getCode().equals(staffRoleResponse.getRoleCode()));

                if (!exists) {
                    Optional<Role> roleOptional = roleRepository.findByCode(staffRoleResponse.getRoleCode());
                    roleOptional.ifPresent(value -> {
                        StaffRole newStaffRole = new StaffRole();
                        newStaffRole.setStaff(savedStaff);
                        newStaffRole.setRole(value);
                        newStaffRole.setStatus(EntityStatus.ACTIVE);
                        staffRoleRepo.save(newStaffRole);
                    });

                    if (roleOptional.isEmpty()) {
                        Role newRole = new Role();
                        newRole.setCode(staffRoleResponse.getRoleCode());
                        newRole.setName(staffRoleResponse.getRoleName());
                        newRole.setFacility(facility);
                        roleRepository.save(newRole);

                        StaffRole newStaffRole = new StaffRole();
                        newStaffRole.setStaff(savedStaff);
                        newStaffRole.setRole(newRole);
                        newStaffRole.setStatus(EntityStatus.ACTIVE);
                        staffRoleRepo.save(newStaffRole);
                    }
                }
            });
        }
    }

}
