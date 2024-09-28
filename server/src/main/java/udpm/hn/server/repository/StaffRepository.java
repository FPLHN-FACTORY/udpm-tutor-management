package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Major;
import udpm.hn.server.entity.Staff;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {

    @Query("""
            SELECT s
            FROM Staff s
            WHERE s.staffIdentityId = :staffIdentityId
            """)
    Optional<Staff> findStaffByStaffIdentityId(Long staffIdentityId);

    @Query("""
    SELECT s
    FROM Staff s
    WHERE (:emailHeadDepartmentFe IS NULL OR :emailHeadDepartmentFe = '' OR s.emailFe = :emailHeadDepartmentFe) 
      AND (:emailHeadDepartmentFpt IS NULL OR :emailHeadDepartmentFpt = '' OR s.emailFpt = :emailHeadDepartmentFpt)
    """)
    Optional<Staff> findStaffByEmail(String emailHeadDepartmentFpt, String emailHeadDepartmentFe);

}
