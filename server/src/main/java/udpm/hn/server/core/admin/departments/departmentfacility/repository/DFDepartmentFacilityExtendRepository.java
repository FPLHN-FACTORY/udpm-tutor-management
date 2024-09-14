package udpm.hn.server.core.admin.departments.departmentfacility.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.CreateOrUpdateDepartmentFacilityRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.FindFacilityDetailRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.model.response.DepartmentFacilityResponse;
import udpm.hn.server.core.admin.departments.departmentfacility.model.response.ListFacilityResponse;
import udpm.hn.server.core.admin.departments.departmentfacility.model.response.ListStaffResponse;
import udpm.hn.server.entity.DepartmentFacility;
import udpm.hn.server.repository.DepartmentFacilityRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DFDepartmentFacilityExtendRepository extends DepartmentFacilityRepository {

    @Query(value = """
            SELECT
                ROW_NUMBER() OVER(
                    ORDER BY df.id DESC
                ) AS orderNumber,
                df.id AS departmentFacilityId,
                df.id_facility AS facilityId,
                f.code as facilityCode,
                st.id AS headOfDepartmentId,
                df.status AS departmentFacilityStatus,
                f.name AS facilityName,
                st.email_fpt AS headOfDepartmentName,
                st.staff_code AS headOfDepartmentCode,
                CONCAT(st.staff_code, ' - ', st.email_fpt) AS profileStaff,
                df.created_date AS createdDate
            FROM
                department_facility df
            LEFT JOIN facility f ON
                f.id = df.id_facility
            LEFT JOIN staff st ON
                st.id = df.id_staff
            WHERE
                (df.id_department = :id) AND
                (:#{#req.facilityName} IS NULL OR f.name LIKE %:#{#req.facilityName}%) AND
                (:#{#req.staffCodeOrEmail} IS NULL OR st.staff_code LIKE %:#{#req.staffCodeOrEmail}% OR st.email_fpt LIKE %:#{#req.staffCodeOrEmail}%)
            """,
            countQuery = """
            SELECT
                COUNT(df.id)
            FROM
                department_facility df
            LEFT JOIN facility f ON
                f.id = df.id_facility
            LEFT JOIN staff st ON
                st.id = df.id_staff
            WHERE
                (df.id_department = :id) AND
                (:#{#req.facilityName} IS NULL OR f.name LIKE %:#{#req.facilityName}%) AND
                (:#{#req.staffCodeOrEmail} IS NULL OR st.staff_code LIKE %:#{#req.staffCodeOrEmail}% OR st.email_fpt LIKE %:#{#req.staffCodeOrEmail}%)
        """,
            nativeQuery = true)
    Page<DepartmentFacilityResponse> getDepartmentFacilitiesByValueFind(String id, Pageable pageable, @Param("req") FindFacilityDetailRequest req);

    @Query("""
            SELECT df FROM DepartmentFacility df
            WHERE df.department.id = :#{#req.departmentId} AND df.facility.id = :#{#req.facilityId}
            """)
    Optional<DepartmentFacility> existsByIdDepartmentAndIdFacilityAndIdAdd(@Param("req") CreateOrUpdateDepartmentFacilityRequest req);


    @Query("""
            SELECT df FROM DepartmentFacility df
            WHERE df.department.id = :departmentId AND df.facility.id = :facilityId
            """)
    Optional<DepartmentFacility> existsDepartmentFacilitiesByDepartmentAndFacility(String departmentId, String facilityId);

    @Query(value = """
            SELECT
                id AS facilityId,
                name AS facilityName
            FROM
                facility
            WHERE
                status = 0
            """, nativeQuery = true)
    List<ListFacilityResponse> getListFacility();

    @Query(value = """
            SELECT
                id AS staffId,
                email_fpt AS staffName,
                staff_code AS staffCode
            FROM
                staff
            WHERE status = 0
            """, nativeQuery = true)
    List<ListStaffResponse> getListStaff();

}