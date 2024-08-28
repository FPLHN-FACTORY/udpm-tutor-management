package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.StaffMajorFacility;

@Repository
public interface StaffMajorFacilityRepository extends JpaRepository<StaffMajorFacility, String> {
}
