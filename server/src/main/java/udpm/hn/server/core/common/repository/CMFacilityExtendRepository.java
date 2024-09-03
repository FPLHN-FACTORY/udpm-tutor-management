package udpm.hn.server.core.common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.common.model.request.CMOptionsFilterRequest;
import udpm.hn.server.core.common.model.response.CMFacilityOptionsResponse;
import udpm.hn.server.repository.FacilityRepository;

import java.util.List;

@Repository
public interface CMFacilityExtendRepository extends FacilityRepository {

    @Query(value = """ 
              SELECT  f.id AS id,
                      f.name AS name
              FROM facility f
              WHERE
                 :#{#request.query} is null or f.name like CONCAT('%', :#{#request.query}, '%')
            """, nativeQuery = true)
    List<CMFacilityOptionsResponse> getFacilities(CMOptionsFilterRequest request);

}
