package udpm.hn.server.infrastructure.config.database;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import udpm.hn.server.infrastructure.config.database.repository.DBGenBlockRepository;
import udpm.hn.server.infrastructure.config.database.repository.DBGenFacilityRepository;
import udpm.hn.server.infrastructure.config.database.repository.DBGenRoleRepository;
import udpm.hn.server.infrastructure.config.database.repository.DBGenSemesterRepository;
import udpm.hn.server.infrastructure.config.database.repository.DBGenStaffRepository;
import udpm.hn.server.infrastructure.config.database.repository.DBGenStaffRoleRepository;
import udpm.hn.server.infrastructure.constant.BlockName;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.SemesterName;
import udpm.hn.server.entity.Block;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.entity.Role;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.StaffRole;

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
        if (isGenerated.equals("true")) generateData();
    }

    private void generateData() {
        Semester semester = new Semester();
        semester.setSemesterName(SemesterName.SPRING);
        semester.setYear(getCurrentYear());
        semester.setStartTime(getStartTime(SemesterName.SPRING, getCurrentYear()));
        semester.setEndTime(getEndTime(SemesterName.SPRING, getCurrentYear()));
        semester.setStatus(EntityStatus.ACTIVE);
        Semester semesterSaved = semesterRepository.save(semester);

        Block blockOne = new Block();
        blockOne.setName(BlockName.BLOCK_1);
        blockOne.setStartTime(getStartTime(SemesterName.SPRING, getCurrentYear()));
        blockOne.setEndTime(getEndTime(SemesterName.SPRING, getCurrentYear()));
        blockOne.setSemester(semesterSaved);
        blockOne.setStatus(EntityStatus.ACTIVE);
        blockRepository.save(blockOne);

        Block blockTwo = new Block();
        blockTwo.setName(BlockName.BLOCK_2);
        blockTwo.setStartTime(getStartTime(SemesterName.SUMMER, getCurrentYear()));
        blockTwo.setEndTime(getEndTime(SemesterName.SUMMER, getCurrentYear()));
        blockTwo.setSemester(semesterSaved);
        blockTwo.setStatus(EntityStatus.ACTIVE);
        blockRepository.save(blockTwo);

        Facility facility = new Facility();
        facility.setCode("HA_NOI");
        facility.setName("Hà Nội");
        facility.setStatus(EntityStatus.ACTIVE);
        facilityRepository.save(facility);

        Role admin = new Role();
        admin.setCode(udpm.hn.server.infrastructure.constant.Role.ADMIN.name());
        admin.setName("Admin");
        admin.setStatus(EntityStatus.ACTIVE);
        Role roleSaved = roleRepository.save(admin);

        Staff staff = new Staff();
        staff.setName("Nguyễn Minh Hiếu");
        staff.setStaffCode("PH42056");
        staff.setEmailFe("hieunmph42056@fe.edu.vn");
        staff.setEmailFpt("hieunmph42056@fpt.edu.vn");
        staff.setStatus(EntityStatus.ACTIVE);
        Staff staffSaved = staffRepository.save(staff);

        StaffRole staffRole = new StaffRole();
        staffRole.setRole(roleSaved);
        staffRole.setStaff(staffSaved);
        staffRole.setStatus(EntityStatus.ACTIVE);
        staffRoleRepository.save(staffRole);

    }

    private static long getStartTime(SemesterName semesterName, int year) {
        LocalDate startDate = switch (semesterName) {
            case SPRING -> LocalDate.of(year, 1, 15);
            case SUMMER -> LocalDate.of(year, 5, 15);
            case FALL -> LocalDate.of(year, 9, 1);
            default -> throw new IllegalArgumentException("Unknown semester: " + semesterName);
        };
        return startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private static long getEndTime(SemesterName semesterName, int year) {
        LocalDate endDate = switch (semesterName) {
            case SPRING -> LocalDate.of(year, 5, 1);
            case SUMMER -> LocalDate.of(year, 8, 31);
            case FALL -> LocalDate.of(year, 12, 15);
            default -> throw new IllegalArgumentException("Unknown semester: " + semesterName);
        };
        return endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private Integer getCurrentYear() {
        return java.time.Year.now().getValue();
    }

}
