package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
}
