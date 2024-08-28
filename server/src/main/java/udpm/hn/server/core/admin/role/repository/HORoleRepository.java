package udpm.hn.server.core.admin.role.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.role.model.request.HORoleRequest;
import udpm.hn.server.core.admin.role.model.response.HORoleResponse;
import udpm.hn.server.entity.Role;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.RoleRepository;

import java.util.List;

@Repository
public interface HORoleRepository extends RoleRepository {

    @Query(value = """
            SELECT r.name as roleName,
                   r.id as idRole,
                   r.code as roleCode,
                   f.name as facilityName
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
            """,
            countQuery = """
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
                    """,
            nativeQuery = true)
    Page<HORoleResponse> getAllRole(Pageable pageable, HORoleRequest hoRoleRequest);

    List<Role> findAllByCodeAndFacility_Id(String code, String facilityId);

    List<Role> findAllByCodeAndFacility_IdAndStatus(String code, String facilityId, EntityStatus status);

}
