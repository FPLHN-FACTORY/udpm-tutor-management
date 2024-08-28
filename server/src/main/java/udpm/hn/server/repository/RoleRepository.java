package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
