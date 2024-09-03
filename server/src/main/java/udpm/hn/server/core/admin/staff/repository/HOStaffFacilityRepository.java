package udpm.hn.server.core.admin.staff.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.staff.model.response.HORoleFacilityResponse;
import udpm.hn.server.repository.FacilityRepository;

import java.util.List;

@Repository
public interface HOStaffFacilityRepository extends FacilityRepository {

    @Query(value = """ 
              SELECT  f.id AS idFacility,
                      f.name AS facilityName
              FROM facility f
              WHERE f.status=0
            """, nativeQuery = true)
    List<HORoleFacilityResponse> getFacilities();

    @Query(value = """ 
            SELECT  f.id AS idFacility,
                    f.name AS facilityName
            FROM facility f
            WHERE f.status=0
            AND f.id NOT IN (SELECT  f.id AS idFacility
                             FROM staff_major_facility smf
                             JOIN major_facility mf ON mf.id = smf.id_major_facility
                             JOIN department_facility df ON df.id = mf.id_department_facility
                             JOIN facility f ON f.id = df.id_facility
                             WHERE f.status=0
                             AND smf.id_staff LIKE :idStaff)
            """, nativeQuery = true)
    List<HORoleFacilityResponse> getFacilitiesSelect(String idStaff);

}
