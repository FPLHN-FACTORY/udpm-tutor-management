package udpm.hn.server.infrastructure.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.repository.StaffRoleRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRoleAuthRepository extends StaffRoleRepository {


    @Query("""
            SELECT c FROM Facility c
            WHERE c.id = :id
            """)
    Optional<Facility> findCampusById(Long id);

    @Query("""
            SELECT DISTINCT r.name FROM Role r
            JOIN StaffRole sr ON r.id = sr.role.id
            WHERE sr.staff.id = :staffId
            """)
    List<String> getRoleNamesByStaffId(String staffId);

    @Query("""
        SELECT DISTINCT r.code FROM Role r
        JOIN StaffRole sr ON r.id = sr.role.id
        WHERE sr.staff.id = :staffId
        """)
    List<String> getRoleCodesByStaffId(String staffId);

}
