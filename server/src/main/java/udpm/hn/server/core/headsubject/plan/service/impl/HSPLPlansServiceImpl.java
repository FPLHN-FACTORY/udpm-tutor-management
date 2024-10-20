package udpm.hn.server.core.headsubject.plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headsubject.plan.model.request.AssignedPlannerRequest;
import udpm.hn.server.core.headsubject.plan.model.request.CreatePlannerRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLPlanInfoRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLPlanListRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLPlannerListRequest;
import udpm.hn.server.core.headsubject.plan.repository.HSPLPlansRepository;
import udpm.hn.server.core.headsubject.plan.repository.HSPLStaffsRepository;
import udpm.hn.server.core.headsubject.plan.service.HSPLPlanService;
import udpm.hn.server.core.planner.plan.model.request.PLPLTutorListRequest;
import udpm.hn.server.entity.Department;
import udpm.hn.server.entity.DepartmentFacility;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.entity.Major;
import udpm.hn.server.entity.MajorFacility;
import udpm.hn.server.entity.Role;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.StaffMajorFacility;
import udpm.hn.server.entity.StaffRole;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.DepartmentFacilityRepository;
import udpm.hn.server.repository.DepartmentRepository;
import udpm.hn.server.repository.FacilityRepository;
import udpm.hn.server.repository.MajorFacilityRepository;
import udpm.hn.server.repository.MajorRepository;
import udpm.hn.server.repository.RoleRepository;
import udpm.hn.server.repository.StaffMajorFacilityRepository;
import udpm.hn.server.repository.StaffRepository;
import udpm.hn.server.repository.StaffRoleRepository;
import udpm.hn.server.utils.Helper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class HSPLPlansServiceImpl implements HSPLPlanService {

    private final HSPLPlansRepository plansRepository;

    private final HSPLStaffsRepository hsplStaffsRepository;

    private final StaffRoleRepository staffRoleRepository;

    private final StaffRepository staffRepository;

    private final RoleRepository roleRepository;

    private final DepartmentFacilityRepository departmentFacilityRepository;

    private final MajorRepository majorRepository;

    private final DepartmentRepository departmentRepository;

    private final FacilityRepository facilityRepository;

    private final MajorFacilityRepository majorFacilityRepository;

    private final StaffMajorFacilityRepository staffMajorFacilityRepository;

    @Override
    public ResponseObject<?> getAllPlans(HSPLPlanListRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(plansRepository.getAllPlanning(pageable, request)),
                HttpStatus.OK,
                "Lấy danh sách kế hoach thành công!"
        );
    }

    @Override
    public ResponseObject<?> getPlansDetail(String planId) {
        return plansRepository.getPlanById(planId)
                .map(plan -> new ResponseObject<>(plan, HttpStatus.OK, "Lấy thông tin môn học thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Môn học không tồn tại"));
    }

    @Override
    public ResponseObject<?> getPlansBySemester(HSPLPlanInfoRequest request) {
        return plansRepository.getSemesterInfo(request)
                .map(plan -> new ResponseObject<>(plan, HttpStatus.OK, "Lấy thông tin học kỳ thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Học kỳ không tồn tại"));
    }

    @Override
    public ResponseObject<?> getPlansInfo(HSPLPlanInfoRequest request) {
        return new ResponseObject<>(
                plansRepository.getPlanInfo(request),
                HttpStatus.OK,
                "Lấy danh sách kế hoach thành công!"
        );
    }

    @Override
    public ResponseObject<?> getPlansInfoById(String id) {
        return plansRepository.getPlanInfoById(id)
                .map(plan -> new ResponseObject<>(plan, HttpStatus.OK, "Lấy thông tin kế hoạch thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Kế hoạch không tồn tại"));
    }

    @Override
    public ResponseObject<?> getAllPlannerManager(HSPLPlannerListRequest request) {
        request.setHeadSubjectRoleCode(udpm.hn.server.infrastructure.constant.Role.NGUOI_LAP_KE_HOACH.name());
        return new ResponseObject<>(
                PageableObject.of(
                        hsplStaffsRepository.getAllPlannerManager(
                                Helper.createPageable(request),
                                request
                        )
                ),
                HttpStatus.OK,
                "Lấy danh sách trưởng bộ môn thành công"
        );
    }

    @Override
    public ResponseObject<?> isAssignedPlanner(String id, AssignedPlannerRequest request) {

        String idStaffRolePlanner = hsplStaffsRepository.findSubjectPlannerIdByRole(request);
        Staff staff = null;
        if (idStaffRolePlanner != null) {
            staff = hsplStaffsRepository.findById(idStaffRolePlanner).orElse(null);
            if (staff != null && staff.getId() != null) {
                // Lấy danh sách StaffRole của nhân viên
                List<StaffRole> staffRoles = staffRoleRepository.findByStaffIdV2(staff.getId());

                // Lặp qua danh sách staffRoles để xóa các role có code là "NGUOI_LAP_KE_HOACH"
                for(StaffRole staffRole : staffRoles) {
                    if(staffRole.getRole().getCode().equals(udpm.hn.server.infrastructure.constant.Role.NGUOI_LAP_KE_HOACH.name())) {
                        staffRoleRepository.delete(staffRole);
                    }
                }
            }
        }

        Staff staffAssigned = staffRepository.findById(id).orElse(null);
        if (staffAssigned == null) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Nhân viên không tồn tại");
        }
        Optional<Role> role = roleRepository.findByCode(udpm.hn.server.infrastructure.constant.Role.NGUOI_LAP_KE_HOACH.name());
        if (staff != null && role.isPresent()) {
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

    public ResponseObject<?> createPlanner(CreatePlannerRequest request) {
        // Kiểm tra mã nhân viên đã tồn tại
        Optional<Staff> existingStaff = staffRepository.findByStaffCodeV2(request.getStaffCode());
        if (existingStaff.isPresent() && existingStaff.get().getStatus() == EntityStatus.ACTIVE) {
            return new ResponseObject<>(null, HttpStatus.CONFLICT, "Mã nhân viên đã tồn tại");
        }

        Optional<Staff> existingStaffCheckEmailFpt = hsplStaffsRepository.findStaffByEmailFptV2(request.getEmailFpt());
        if (existingStaffCheckEmailFpt.isPresent() && existingStaffCheckEmailFpt.get().getStatus() == EntityStatus.ACTIVE) {
            return new ResponseObject<>(null, HttpStatus.CONFLICT, "Email Fpt đã tồn tại");
        }

        Optional<Staff> existingStaffCheckEmailFe = hsplStaffsRepository.findStaffByEmailFeV2(request.getEmailFe());
        if (existingStaffCheckEmailFe.isPresent() && existingStaffCheckEmailFe.get().getStatus() == EntityStatus.ACTIVE) {
            return new ResponseObject<>(null, HttpStatus.CONFLICT, "Email Fe đã tồn tại");
        }


        // Tạo mới nhân viên
        Staff staff = new Staff();
        staff.setName(request.getName());
        staff.setStatus(EntityStatus.ACTIVE);
        staff.setStaffCode(request.getStaffCode());
        staff.setEmailFe(request.getEmailFe());
        staff.setEmailFpt(request.getEmailFpt());
        Staff staffResult = staffRepository.save(staff);

        // Lấy vai trò "NGUOI_LAP_KE_HOACH"
        Optional<Role> roleOptional = roleRepository.findByCode(udpm.hn.server.infrastructure.constant.Role.NGUOI_LAP_KE_HOACH.name());

        if (staffResult != null && roleOptional.isPresent()) {
            Role role = roleOptional.get();

            // Xóa vai trò của nhân viên đã được phân công lập kế hoạch trước đó
            String idStaffRolePlanner = hsplStaffsRepository.findSubjectPlannerIdByRole(request.toAssignedPlannerRequest());
            if (idStaffRolePlanner != null) {
                hsplStaffsRepository.findById(idStaffRolePlanner).ifPresent(staffCheck -> {
                    List<StaffRole> staffRoles = staffRoleRepository.findByStaffIdV2(staffCheck.getId());

                    // Sử dụng Stream API để xóa các role có code là "NGUOI_LAP_KE_HOACH"
                    staffRoles.stream()
                            .filter(staffRole -> staffRole.getRole().getCode().equals(udpm.hn.server.infrastructure.constant.Role.NGUOI_LAP_KE_HOACH.name()))
                            .forEach(staffRoleRepository::delete);
                });
            }

            // Gán vai trò "NGUOI_LAP_KE_HOACH" cho nhân viên mới
            StaffRole newStaffRole = new StaffRole();
            newStaffRole.setRole(role);
            newStaffRole.setStaff(staffResult);
            staffRoleRepository.save(newStaffRole);

            // Kiểm tra department, major, facility có tồn tại không trước khi thực hiện các thao tác tiếp theo
            Optional<Department> departmentOptional = departmentRepository.findByCode(request.getCurrentDepartmentCode());
            Optional<Facility> facilityOptional = facilityRepository.findByCode(request.getCurrentFacilityCode());

            if (departmentOptional.isPresent() && facilityOptional.isPresent()) {
                Department department = departmentOptional.get();
                Facility facility = facilityOptional.get();

                // Kiểm tra major từ department
                Optional<Major> majorOptional = majorRepository.findByDepartment(department);
                if (majorOptional.isPresent()) {
                    Major major = majorOptional.get();

                    // Tạo và lưu DepartmentFacility
                    DepartmentFacility departmentFacility = new DepartmentFacility();
                    departmentFacility.setDepartment(department);
                    departmentFacility.setStaff(staffResult);
                    departmentFacility.setFacility(facility);
                    departmentFacility.setStatus(EntityStatus.ACTIVE);
                    departmentFacility.setCreatedDate(System.currentTimeMillis());

                    DepartmentFacility departmentFacilityResult = departmentFacilityRepository.save(departmentFacility);

                    // Tạo và lưu MajorFacility
                    MajorFacility majorFacility = new MajorFacility();
                    majorFacility.setDepartmentFacility(departmentFacilityResult);
                    majorFacility.setMajor(major);
                    majorFacility.setStaff(staffResult);
                    majorFacility.setStatus(EntityStatus.ACTIVE);
                    majorFacility.setCreatedDate(System.currentTimeMillis());
                    MajorFacility majorFacilityResult = majorFacilityRepository.save(majorFacility);

                    if(majorFacilityResult != null) {
                        StaffMajorFacility staffMajorFacility = new StaffMajorFacility();
                        staffMajorFacility.setCreatedDate(System.currentTimeMillis());
                        staffMajorFacility.setStatus(EntityStatus.ACTIVE);
                        staffMajorFacility.setStaff(staffResult);
                        staffMajorFacility.setMajorFacility(majorFacilityResult);
                        staffMajorFacilityRepository.save(staffMajorFacility);
                    }


                } else {
                    return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lỗi hệ thống");
                }
            } else {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lỗi hệ thống");
            }
        }

        // Trả về phản hồi thành công với thông tin nhân viên vừa tạo
        return new ResponseObject<>(staffResult, HttpStatus.OK, "Tạo thành công người lập kế hoạch");
    }


}
