package udpm.hn.server.core.admin.staff.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.staff.model.request.HOStaffRoleRequest;
import udpm.hn.server.core.admin.staff.model.response.HOStaffRoleCheckResponse;
import udpm.hn.server.core.admin.staff.model.response.HOStaffRoleResponse;
import udpm.hn.server.entity.StaffRole;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.StaffRoleRepository;

import java.util.List;

@Repository
public interface HOStaffRoleRepository extends StaffRoleRepository {

    @Query(value = """
            SELECT
               ROW_NUMBER() OVER(
                        ORDER BY r.id DESC) AS orderNumber,
               r.name AS roleName,
               r.code AS roleCode,
               r.id AS roleId,
               f.name AS facilityName
            FROM staff_role sr
            LEFT JOIN role r ON sr.id_role = r.id
            LEFT JOIN facility f ON r.id_facility = f.id
            WHERE sr.status = 0
            AND sr.id_staff = :staffId
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM staff_role sr
                    LEFT JOIN role r ON sr.id_role = r.id
                    LEFT JOIN facility f ON r.id_facility = f.id
                    WHERE sr.status = 0
                    AND sr.id_staff = :staffId
                    """,
            nativeQuery = true)
    List<HOStaffRoleResponse> getRolesByStaffId(String staffId);

    @Query(value = """ 
            SELECT r.name AS roleName,
                   r.id AS idRole,
                   r.code AS roleCode,
                   f.name AS facilityName,
                   IF(r.id IN (SELECT sr.id_role FROM staff_role sr
                                                   WHERE sr.id_staff LIKE :#{#hoRoleRequest.staffId}
                                                   AND sr.status = 0), 'true', 'false') as checked
                  FROM role r
                  LEFT JOIN facility f ON r.id_facility = f.id
                  WHERE r.status = 0
                  AND (:#{#hoRoleRequest.roleName} IS NULL 
                           OR :#{#hoRoleRequest.roleName} LIKE '' 
                           OR r.name LIKE %:#{#hoRoleRequest.roleName}%
                           OR r.code LIKE %:#{#hoRoleRequest.roleName}%)
                  AND (:#{#hoRoleRequest.idFacility} IS NULL 
                           OR :#{#hoRoleRequest.idFacility} LIKE '' 
                           OR r.id_facility LIKE :#{#hoRoleRequest.idFacility})
                  ORDER BY r.last_modified_date desc
              """, countQuery = """
                      SELECT COUNT(*)
                      FROM role r
                      LEFT JOIN facility f ON r.id_facility = f.id
                      WHERE r.status = 0
                      AND (:#{#hoRoleRequest.roleName} IS NULL 
                               OR :#{#hoRoleRequest.roleName} LIKE '' 
                               OR r.name LIKE %:#{#hoRoleRequest.roleName}%
                               OR r.code LIKE %:#{#hoRoleRequest.roleName}%)
                      AND (:#{#hoRoleRequest.idFacility} IS NULL 
                               OR :#{#hoRoleRequest.idFacility} LIKE '' 
                               OR r.id_facility LIKE :#{#hoRoleRequest.idFacility})
                      ORDER BY r.last_modified_date desc
            """, nativeQuery = true)
    Page<HOStaffRoleCheckResponse> getRolesChecked(Pageable pageable, HOStaffRoleRequest hoRoleRequest);

    List<StaffRole> findAllByRole_IdAndStaff_Id(String roleId, String staffId);

    List<StaffRole> findAllByStaff_IdAndStatus(String staffId, EntityStatus status);

}
