package udpm.hn.server.core.admin.role.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.role.model.response.HORoleFacilityResponse;
import udpm.hn.server.repository.FacilityRepository;

import java.util.List;

@Repository
public interface HORoleFacilityRepository extends FacilityRepository {
    @Query(value = """ 
              SELECT  f.id AS idFacility,
                      f.name AS facilityName
              FROM facility f
              WHERE f.status=0
            """, nativeQuery = true)
    List<HORoleFacilityResponse> getFacilities();
}
