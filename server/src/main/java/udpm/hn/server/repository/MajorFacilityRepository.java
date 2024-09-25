package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Major;
import udpm.hn.server.entity.MajorFacility;

import java.util.Optional;

@Repository
public interface MajorFacilityRepository extends JpaRepository<MajorFacility, String> {

    @Query("""
            SELECT mf
            FROM MajorFacility mf
            WHERE mf.majorFacilityIdentityId = :majorFacilityIdentityId
            """)
    Optional<MajorFacility> findMajorFacilityByMajorFacilityIdentityId(Long majorFacilityIdentityId);

}
