package udpm.hn.server.infrastructure.config.job.staff.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Role;
import udpm.hn.server.repository.RoleRepository;

import java.util.List;

@Repository
public interface ConfigRoleCustomRepository extends RoleRepository {

    @Query(
            value = """
                    SELECT CONCAT(r.name,' - ',f.name) AS roleName
                    FROM role r
                    LEFT JOIN facility f ON f.id = r.id_facility
                    WHERE r.status = 0 AND f.id = :idFacility
                    """,
            nativeQuery = true
    )
    List<String> findAllByFacilityId(String idFacility);

    @Query(
            value = """
                     SELECT DISTINCT r
                     FROM Role r
                     WHERE r.name = :roleName
                    """)
    List<Role> findAllByRoleName(String roleName);

    @Query(
            value = """
                     SELECT DISTINCT r
                     FROM Role r
                     JOIN FETCH r.facility f
                     WHERE r.name = :roleName
                     AND f.name = :facilityName
                    """)
    List<Role> findAllByRoleNameAndFacilityName(String roleName, String facilityName);

}
