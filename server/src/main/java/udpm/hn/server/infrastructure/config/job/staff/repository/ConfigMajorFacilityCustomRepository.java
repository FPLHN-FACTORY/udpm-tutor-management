package udpm.hn.server.infrastructure.config.job.staff.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.MajorFacility;
import udpm.hn.server.repository.MajorFacilityRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigMajorFacilityCustomRepository extends MajorFacilityRepository {

    @Query("""
                    SELECT DISTINCT mf
                    FROM MajorFacility mf
                    WHERE mf.departmentFacility.department.name = :departmentName
                    AND mf.departmentFacility.facility.code = :facilityCode
                    AND mf.major.name = :majorName
            """)
    List<MajorFacility> getMajorFacilities(String departmentName, String majorName, String facilityCode);

    Optional<MajorFacility> findByDepartmentFacility_Facility_Code(String facilityCode);

}
