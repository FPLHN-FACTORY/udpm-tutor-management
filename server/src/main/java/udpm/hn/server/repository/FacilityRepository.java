package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.entity.Semester;

import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, String> {

    @Query("""
            SELECT f
            FROM Facility f
            WHERE f.id  = :facilityId
            """)
    Optional<Facility> findByFacilityId(long facilityId);

}
