package udpm.hn.server.core.common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.common.model.request.RoleSearchRequest;
import udpm.hn.server.core.common.model.response.CMRoleOptionsResponse;
import udpm.hn.server.repository.RoleRepository;

import java.util.List;

@Repository
public interface CMRoleExtendRepository extends RoleRepository {

    @Query(
            value = """
                    SELECT
                        r.id AS id,
                        r.name AS name
                    FROM role r LEFT JOIN facility f ON r.id_facility = f.id
                    WHERE f.code = :#{#request.facilityCode}
                    """,
            nativeQuery = true
    )
    List<CMRoleOptionsResponse> getRoles(RoleSearchRequest request);

}
