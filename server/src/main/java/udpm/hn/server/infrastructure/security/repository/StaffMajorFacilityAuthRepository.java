package udpm.hn.server.infrastructure.security.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.StaffMajorFacility;
import udpm.hn.server.repository.StaffMajorFacilityRepository;

import java.util.Optional;

@Repository
public interface StaffMajorFacilityAuthRepository extends StaffMajorFacilityRepository {

    Optional<StaffMajorFacility> findByStaffId(String staffId);

}
