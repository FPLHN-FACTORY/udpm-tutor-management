package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Department;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    @Query("""
            SELECT d
            FROM Department d
            WHERE d.departmentIdentityId = :departmentIdentityId
            """)
    Optional<Department> findDepartmentByDepartmentIdentityId(Long departmentIdentityId);

    Optional<Department> findByCode(String code);

}
