package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.DepartmentFacility;

@Repository
public interface DepartmentFacilityRepository extends JpaRepository<DepartmentFacility, String> {
}
