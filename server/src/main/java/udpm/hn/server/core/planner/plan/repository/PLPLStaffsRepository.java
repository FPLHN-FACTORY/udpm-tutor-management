package udpm.hn.server.core.planner.plan.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.repository.StaffRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PLPLStaffsRepository extends StaffRepository {
    Optional<Staff> findByStaffCode(String userCode);

    @Query(
            value = """
            SELECT
                s.email_fe
            FROM
                staff s
            LEFT JOIN staff_major_facility smf ON s.id = smf.id_staff
            LEFT JOIN major_facility mf ON mf.id = smf.id_major_facility
            LEFT JOIN department_facility df ON mf.id_department_facility = df.id
            LEFT JOIN facility f ON f.id = df.id_facility
            LEFT JOIN department d ON d.id = df.id_department
            LEFT JOIN staff_role sr ON sr.id_staff = s.id
            LEFT JOIN role r ON r.id = sr.id_role
            WHERE
                r.code IN :role
                AND d.code LIKE CONCAT('%', :departmentCode, '%')
                AND f.code LIKE CONCAT('%', :facilityCode, '%')
            """,
            nativeQuery = true
    )
    List<String> getStaffsByRole(List<String> role, String departmentCode, String facilityCode);

    @Query(
            value = """
            SELECT
                s.staff_code
            FROM
                staff s
            LEFT JOIN staff_major_facility smf ON s.id = smf.id_staff
            LEFT JOIN major_facility mf ON mf.id = smf.id_major_facility
            LEFT JOIN department_facility df ON mf.id_department_facility = df.id
            LEFT JOIN facility f ON f.id = df.id_facility
            LEFT JOIN department d ON d.id = df.id_department
            LEFT JOIN staff_role sr ON sr.id_staff = s.id
            LEFT JOIN role r ON r.id = sr.id_role
            WHERE
                r.code IN :role
                AND d.code LIKE CONCAT('%', :departmentCode, '%')
                AND f.code LIKE CONCAT('%', :facilityCode, '%')
            """,
            nativeQuery = true
    )
    List<String> getStaffCodesByRole(List<String> role, String departmentCode, String facilityCode);
}
