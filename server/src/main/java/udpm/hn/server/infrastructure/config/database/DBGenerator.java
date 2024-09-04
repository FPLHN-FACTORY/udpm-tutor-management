package udpm.hn.server.infrastructure.config.database;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import udpm.hn.server.entity.Block;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.entity.Role;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.StaffRole;
import udpm.hn.server.infrastructure.config.database.repository.DBGenBlockRepository;
import udpm.hn.server.infrastructure.config.database.repository.DBGenFacilityRepository;
import udpm.hn.server.infrastructure.config.database.repository.DBGenRoleRepository;
import udpm.hn.server.infrastructure.config.database.repository.DBGenSemesterRepository;
import udpm.hn.server.infrastructure.config.database.repository.DBGenStaffRepository;
import udpm.hn.server.infrastructure.config.database.repository.DBGenStaffRoleRepository;
import udpm.hn.server.infrastructure.constant.BlockName;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.SemesterName;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class DBGenerator {

    private final DBGenRoleRepository roleRepository;

    private final DBGenStaffRepository staffRepository;

    private final DBGenStaffRoleRepository staffRoleRepository;

    private final DBGenFacilityRepository facilityRepository;

    private final DBGenSemesterRepository semesterRepository;

    private final DBGenBlockRepository blockRepository;

    @Value("${db.generator.is-generated}")
    private String isGenerated;

    @PostConstruct
    public void init() {
        if ("true".equals(isGenerated)) generateData();
    }

    private void generateData() {
        Semester semester = createSemester(SemesterName.SPRING, getCurrentYear());
        semesterRepository.save(semester);

        createAndSaveBlock(BlockName.BLOCK_1, SemesterName.SPRING, semester);
        createAndSaveBlock(BlockName.BLOCK_2, SemesterName.SUMMER, semester);

        Facility facility = new Facility();
        facility.setCode("HA_NOI");
        facility.setName("Hà Nội");
        facility.setStatus(EntityStatus.ACTIVE);
        facilityRepository.save(facility);

        Role roleSaved = roleRepository.findByCodeAndNameAndFacility("ADMIN", "Admin", facility)
                .orElseGet(() -> {
                    Role admin = new Role();
                    admin.setCode(udpm.hn.server.infrastructure.constant.Role.ADMIN.name());
                    admin.setName("Admin");
                    admin.setStatus(EntityStatus.ACTIVE);
                    return roleRepository.save(admin);
                });

        Staff[] staffArray = {
                createStaff("Nguyễn Minh Hiếu", "PH42056", "hieunmph42056@fe.edu.vn", "hieunmph42056@fpt.edu.vn"),
                createStaff("Nguyễn Phi Hùng", "PH42118", "hungnpph42118@fe.edu.vn", "hungnpph42118@fpt.edu.vn"),
                createStaff("Đỗ Văn Công", "PH31357", "congdvph31357@fe.edu.vn", "congdvph31357@fpt.edu.vn"),
                createStaff("Lê Bá Minh", "PH43017", "minhlbph43017@fe.edu.vn", "minhlbph43017fpt.eduv.vn"),
                createStaff("Nguyễn Thị Vân", "PH43399", "vanntph43399@fe.edu.vn", "vanntph43399@fpt.edu.vn")
        };

        for (Staff staff : staffArray) {
            Staff savedStaff = staffRepository.save(staff);
            StaffRole staffRole = new StaffRole();
            staffRole.setRole(roleSaved);
            staffRole.setStaff(savedStaff);
            staffRole.setStatus(EntityStatus.ACTIVE);
            staffRoleRepository.save(staffRole);
        }
    }

    private Semester createSemester(SemesterName semesterName, int year) {
        Semester semester = new Semester();
        semester.setSemesterName(semesterName);
        semester.setYear(year);
        semester.setStartTime(getStartTime(semesterName, year));
        semester.setEndTime(getEndTime(semesterName, year));
        semester.setStatus(EntityStatus.ACTIVE);
        return semester;
    }

    private void createAndSaveBlock(BlockName blockName, SemesterName semesterName, Semester semester) {
        Block block = new Block();
        block.setName(blockName);
        block.setStartTime(getStartTime(semesterName, getCurrentYear()));
        block.setEndTime(getEndTime(semesterName, getCurrentYear()));
        block.setSemester(semester);
        block.setStatus(EntityStatus.ACTIVE);
        blockRepository.save(block);
    }

    private Staff createStaff(String name, String staffCode, String emailFe, String emailFpt) {
        Staff staff = new Staff();
        staff.setName(name);
        staff.setStaffCode(staffCode);
        staff.setEmailFe(emailFe);
        staff.setEmailFpt(emailFpt);
        staff.setStatus(EntityStatus.ACTIVE);
        return staff;
    }

    private static long getStartTime(SemesterName semesterName, int year) {
        LocalDate startDate = switch (semesterName) {
            case SPRING -> LocalDate.of(year, 1, 15);
            case SUMMER -> LocalDate.of(year, 5, 15);
            case FALL -> LocalDate.of(year, 9, 1);
        };
        return startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private static long getEndTime(SemesterName semesterName, int year) {
        LocalDate endDate = switch (semesterName) {
            case SPRING -> LocalDate.of(year, 5, 1);
            case SUMMER -> LocalDate.of(year, 8, 31);
            case FALL -> LocalDate.of(year, 12, 15);
        };
        return endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private int getCurrentYear() {
        return java.time.Year.now().getValue();
    }

}