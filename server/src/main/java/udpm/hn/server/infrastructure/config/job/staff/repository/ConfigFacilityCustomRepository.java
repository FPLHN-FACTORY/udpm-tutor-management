package udpm.hn.server.infrastructure.config.job.staff.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.repository.FacilityRepository;

import java.util.Optional;

@Repository
public interface ConfigFacilityCustomRepository extends FacilityRepository {

    Optional<Facility> findByCode(String facilityCode);

}
