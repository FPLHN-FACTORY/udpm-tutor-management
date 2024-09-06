package udpm.hn.server.infrastructure.config.database.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.repository.FacilityRepository;

import java.util.Optional;

@Repository
public interface DBGenFacilityRepository extends FacilityRepository {

    Optional<Facility> findByCode(String code);

}
