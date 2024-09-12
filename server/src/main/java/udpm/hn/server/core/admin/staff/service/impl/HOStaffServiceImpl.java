package udpm.hn.server.core.admin.staff.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.admin.staff.model.request.HOSaveStaffRequest;
import udpm.hn.server.core.admin.staff.model.request.HOStaffRequest;
import udpm.hn.server.core.admin.staff.repository.HOStaffDepartmentFacilityRepository;
import udpm.hn.server.core.admin.staff.repository.HOStaffRepository;
import udpm.hn.server.core.admin.staff.repository.HOStaffRoleRepository;
import udpm.hn.server.core.admin.staff.service.HOStaffService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.StaffRole;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.utils.Helper;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HOStaffServiceImpl implements HOStaffService {

    private final HOStaffRepository staffRepo;

    private final HOStaffDepartmentFacilityRepository departmentFacilityRepo;

    private final HOStaffRoleRepository staffRoleRepo;

    @Override
    public ResponseObject<?> getStaffByRole(HOStaffRequest hoRoleStaffRequest) {
        Pageable page = Helper.createPageable(hoRoleStaffRequest, "createdDate");
        return new ResponseObject<>(
                        PageableObject.of(staffRepo.getStaffs(page, hoRoleStaffRequest)),
                        HttpStatus.OK,
                "get staffs successfully");
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

        Staff staff = new Staff();

        staff.setName(staffRequest.getName());

        List<Staff> staffList = staffRepo.findByStaffCode(staffRequest.getStaffCode());

        if (!staffList.isEmpty() && staffList.get(0).getStatus() == EntityStatus.ACTIVE) {
            return new ResponseObject<>(null, HttpStatus.CONFLICT, "Mã nhân viên đã tồn tại");
        }

        staff.setStatus(EntityStatus.ACTIVE);
        staff.setStaffCode(staffRequest.getStaffCode());
        staff.setEmailFe(staffRequest.getEmailFe());
        staff.setEmailFpt(staffRequest.getEmailFpt());
        staffRepo.save(staff);

        return new ResponseObject<>(null, HttpStatus.CREATED, "Thêm nhân viên thành công");
    }

    @Override
    public ResponseObject<?> updateStaff(HOSaveStaffRequest staffRequest) {

        Optional<Staff> staffOptional = staffRepo.findById(staffRequest.getId());

        if (staffOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên");
        }

        Staff staff = staffOptional.get();
        staff.setId(staffRequest.getId());
        staff.setName(staffRequest.getName());
        List<Staff> checkStaff = staffRepo.findByStaffCode(staffRequest.getStaffCode());

        if (!checkStaff.isEmpty() &&
            !checkStaff.get(0).getId().equalsIgnoreCase(staffRequest.getStaffCode()) &&
            checkStaff.get(0).getStatus() == EntityStatus.ACTIVE) {
            if (!staff.getId().equals(checkStaff.get(0).getId())) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Mã nhân viên đã tồn tại");
            }
        }

        staff.setStaffCode(staffRequest.getStaffCode());
        staff.setEmailFe(staffRequest.getEmailFe());
        staff.setEmailFpt(staffRequest.getEmailFpt());
        staffRepo.save(staff);

        return new ResponseObject<>(null, HttpStatus.OK, "Sửa nhân viên thành công");
    }

    @Override
    public ResponseObject<?> deleteStaff(String idStaff) {
        Optional<Staff> staffOptional = staffRepo.findById(idStaff);
        if (staffOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "staff not found");
        }
        //change status in staff_role
        List<StaffRole> staffRoles = staffRoleRepo.findAllByStaff_IdAndStatus(idStaff, EntityStatus.ACTIVE);
        staffRoles.stream().forEach(staffRole -> staffRole.setStatus(EntityStatus.INACTIVE));
        staffRoleRepo.saveAll(staffRoles);
        //change status in staff
        staffOptional.get().setStatus(EntityStatus.INACTIVE);
        staffRepo.save(staffOptional.get());
        return new ResponseObject<>(null, HttpStatus.OK, "delete staff successfully");
    }

}
