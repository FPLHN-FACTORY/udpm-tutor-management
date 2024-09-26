package udpm.hn.server.core.planner.plan.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.DepartmentFacility;
import udpm.hn.server.repository.DepartmentFacilityRepository;

import java.util.Optional;

@Repository
public interface PLPLDepartmentFacilitysRepository extends DepartmentFacilityRepository {

    Optional<DepartmentFacility> findByDepartment_CodeAndFacilityCode(String departmentCode, String facilityCode);
}
