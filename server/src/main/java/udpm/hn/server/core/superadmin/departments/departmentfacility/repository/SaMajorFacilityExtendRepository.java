package udpm.hn.server.core.superadmin.departments.departmentfacility.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.superadmin.departments.departmentfacility.model.request.MajorFacilityRequest;
import udpm.hn.server.core.superadmin.departments.departmentfacility.model.response.FacilityDepartmentInfoResponse;
import udpm.hn.server.core.superadmin.departments.departmentfacility.model.response.MajorFacilityDetailResponse;
import udpm.hn.server.core.superadmin.departments.departmentfacility.model.response.MajorFacilityResponse;
import udpm.hn.server.entity.MajorFacility;
import udpm.hn.server.repository.MajorFacilityRepository;

import java.util.Optional;

@Repository
public interface SaMajorFacilityExtendRepository extends MajorFacilityRepository {

    @Query(
            value = """
                    SELECT
                        ROW_NUMBER() OVER(
                        ORDER BY mf.id DESC) AS orderNumber,
                        mf.id as id,
                        CONCAT(m.code, ' - ', m.name) as majorName,
                        CONCAT(s.staff_code, ' - ', s.name) as headMajorCodeName,
                        mf.status as status
                    FROM
                        major_facility mf
                    LEFT JOIN major m ON mf.id_major = m.id
                    LEFT JOIN department_facility df ON mf.id_department_facility = df.id
                    LEFT JOIN staff s ON mf.id_staff = s.id
                    LEFT JOIN facility f ON df.id_facility = f.id
                    LEFT JOIN department d ON df.id_department = d.id
                    WHERE
                        df.id = :#{#request.departmentFacilityId}
                    AND (:#{#request.majorCodeOrName} IS NULL OR m.name LIKE CONCAT('%', :#{#request.majorCodeOrName}, '%') OR m.code LIKE CONCAT('%', :#{#request.majorCodeOrName}, '%'))
                    AND (:#{#request.staffCodeOrName} IS NULL OR s.name LIKE CONCAT('%', :#{#request.staffCodeOrName}, '%') OR s.staff_code LIKE CONCAT('%', :#{#request.staffCodeOrName}, '%'))
                    """,
            countQuery = """
                    SELECT
                        COUNT(mf.id)
                    FROM
                        major_facility mf
                    LEFT JOIN major m ON mf.id_major = m.id
                    LEFT JOIN department_facility df ON mf.id_department_facility = df.id
                    LEFT JOIN staff s ON mf.id_staff = s.id
                    LEFT JOIN facility f ON df.id_facility = f.id
                    WHERE
                        df.id = :#{#request.departmentFacilityId}
                    AND (:#{#request.majorCodeOrName} IS NULL OR m.name LIKE CONCAT('%', :#{#request.majorCodeOrName}, '%') OR m.code LIKE CONCAT('%', :#{#request.majorCodeOrName}, '%'))
                    AND (:#{#request.staffCodeOrName} IS NULL OR s.name LIKE CONCAT('%', :#{#request.staffCodeOrName}, '%') OR s.staff_code LIKE CONCAT('%', :#{#request.staffCodeOrName}, '%'))
                    """,
            nativeQuery = true
    )
    Page<MajorFacilityResponse> findAllMajorFacilities(@Param("request") MajorFacilityRequest request, Pageable pageable);
    
    @Query(
            value = """
                    SELECT
                             f.name as facilityName,
                             d.id as departmentId,
                             d.name as departmentName
                    FROM
                        department_facility df
                    LEFT JOIN facility f ON df.id_facility = f.id
                    LEFT JOIN department d ON df.id_department = d.id
                    WHERE
                        df.id = :departmentFacilityId
                    """,
            nativeQuery = true
    )
    FacilityDepartmentInfoResponse getFacilityDepartmentInfo(String departmentFacilityId);

    @Query(
            value = """
                    SELECT
                        mf.id as id,
                        m.id as majorId,
                        s.id as headMajorId
                    FROM
                        major_facility mf
                    LEFT JOIN major m ON mf.id_major = m.id
                    LEFT JOIN department_facility df ON mf.id_department_facility = df.id
                    LEFT JOIN staff s ON mf.id_staff = s.id
                    LEFT JOIN facility f ON df.id_facility = f.id
                    WHERE
                        mf.id = :majorFacilityId
                    """,
            nativeQuery = true
    )
    Optional<MajorFacilityDetailResponse> findMajorFacilityById(String majorFacilityId);

    Optional<MajorFacility> findByMajor_IdAndDepartmentFacility_Id(String majorId, String departmentFacilityId);

}