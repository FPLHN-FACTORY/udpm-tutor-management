package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.StaffRole;

import java.util.List;

@Repository
public interface StaffRoleRepository extends JpaRepository<StaffRole, String> {

    @Query("""
        select sr from StaffRole sr where sr.staff.id = :staffId
    """)
    List<StaffRole> findByStaffIdV2(String staffId);

}
