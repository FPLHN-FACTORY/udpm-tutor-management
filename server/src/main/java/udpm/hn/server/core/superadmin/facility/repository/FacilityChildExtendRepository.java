package udpm.hn.server.core.superadmin.facility.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.superadmin.facility.model.response.FacilityChildResponse;
import udpm.hn.server.entity.FacilityChild;
import udpm.hn.server.repository.FacilityChildRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacilityChildExtendRepository extends FacilityChildRepository {

    @Query(
            value = """
                    SELECT
                        ROW_NUMBER() OVER (ORDER BY fac.id DESC ) as orderNumber,
                        fac.id as id,
                        fac.code as facilityChildCode,
                        fac.name as facilityChildName,
                        fac.status as facilityChildStatus,
                        fac.created_date as createdDate
                    FROM
                        facility_child fac
                    LEFT JOIN
                         facility fa on fa.id = fac.id_facility
                    WHERE fa.id = :facilityId
                    """,
            countQuery = """
                    SELECT
                        COUNT(DISTINCT fac.id)
                    FROM
                        facility_child fac
                    LEFT JOIN
                         facility fa on fa.id = fac.id_facility
                    WHERE fa.id = :facilityId
                    """,
            nativeQuery = true
    )
    Page<FacilityChildResponse> getAllFacilityChild(String facilityId, Pageable pageable);

    Optional<FacilityChild> findByName(String name);

    @Query(
            value = """
                    SELECT
                        fac.id as id,
                        fac.code as facilityChildCode,
                        fac.name as facilityChildName,
                        fac.status as facilityChildStatus,
                        fac.created_date as createdDate
                    FROM
                        facility_child fac
                    WHERE
                        fac.id = :facilityChildId
                    """,
            nativeQuery = true
    )
    Optional<FacilityChildResponse> getDetailFacilityChildById(String facilityChildId);

    List<FacilityChild> findAllByCode(String facilityCode);

}
