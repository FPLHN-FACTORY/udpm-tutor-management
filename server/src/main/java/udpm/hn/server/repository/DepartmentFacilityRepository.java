package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.DepartmentFacility;
import udpm.hn.server.entity.MajorFacility;

import java.util.Optional;

@Repository
public interface DepartmentFacilityRepository extends JpaRepository<DepartmentFacility, String> {

    @Query("""
            SELECT d
            FROM DepartmentFacility d
            WHERE d.departmentFacilityIdentityId = :departmentFacilityIdentityId
            """)
    Optional<DepartmentFacility> findDepartmentFacilityByDepartmentFacility(Long departmentFacilityIdentityId);

}
