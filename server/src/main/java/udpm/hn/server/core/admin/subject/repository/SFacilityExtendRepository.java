package udpm.hn.server.core.admin.subject.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.repository.FacilityRepository;

import java.util.List;

@Repository
public interface SFacilityExtendRepository extends FacilityRepository {

    @Query("""
            SELECT f
            FROM Facility f
            WHERE f.status = 0
            """)
    List<Facility> getListFacility();

}
