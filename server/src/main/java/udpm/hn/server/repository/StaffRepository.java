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
    Optional<Major> findMajorByStaffIdentityId(Long staffIdentityId);

}
