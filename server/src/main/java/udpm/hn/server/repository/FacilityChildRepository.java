package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.FacilityChild;

@Repository
public interface FacilityChildRepository extends JpaRepository<FacilityChild, String> {
}
