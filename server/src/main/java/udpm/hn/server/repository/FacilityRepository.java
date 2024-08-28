package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Facility;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, String> {

}
