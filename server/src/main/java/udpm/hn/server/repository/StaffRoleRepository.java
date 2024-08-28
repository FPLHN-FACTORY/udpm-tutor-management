package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.StaffRole;

@Repository
public interface StaffRoleRepository extends JpaRepository<StaffRole, String> {
}
