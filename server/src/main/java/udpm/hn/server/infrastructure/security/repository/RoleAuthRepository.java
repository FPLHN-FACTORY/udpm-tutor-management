package udpm.hn.server.infrastructure.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Role;
import udpm.hn.server.infrastructure.security.response.TokenFacilityResponse;
import udpm.hn.server.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleAuthRepository extends RoleRepository {

    @Query("SELECT r FROM Role r WHERE r.code = 'GIANG_VIEN'")
    Optional<Role> findRoleStaff();

    @Query(
            value = """
                    SELECT DISTINCT
                        r.code
                    FROM
                        role r
                    LEFT JOIN
                     staff_role sr ON r.id = sr.id_role
                    WHERE
                        sr.id_staff = :id
                    """,
            nativeQuery = true
    )
    List<String> findRoleByStaffId(String id);

    @Query(
            value = """
                    SELECT DISTINCT
                        f.id AS facilityId,
                        f.code AS facilityCode
                    FROM
                        staff st
                    LEFT JOIN
                     staff_role sr ON st.id = sr.id_staff
                    LEFT JOIN
                     role r ON r.id = sr.id_role
                    LEFT JOIN
                     facility f ON f.id = r.id_facility
                    WHERE
                        sr.id_staff = :id
                    """,
            nativeQuery = true
    )
    TokenFacilityResponse findFacilityByStaffId(String id);

}
