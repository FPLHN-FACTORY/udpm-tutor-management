package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
}
