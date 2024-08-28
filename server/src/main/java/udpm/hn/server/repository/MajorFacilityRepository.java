package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.MajorFacility;

@Repository
public interface MajorFacilityRepository extends JpaRepository<MajorFacility, String> {
}
