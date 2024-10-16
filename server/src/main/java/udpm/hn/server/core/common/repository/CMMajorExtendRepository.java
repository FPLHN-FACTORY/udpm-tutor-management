package udpm.hn.server.core.common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.common.model.request.MajorSearchRequest;
import udpm.hn.server.core.common.model.response.CMMajorOptionsResponse;
import udpm.hn.server.repository.MajorRepository;

import java.util.List;

@Repository
public interface CMMajorExtendRepository extends MajorRepository {

    @Query(
            value = """
                        SELECT
                            DISTINCT
                            m.id AS id,
                            m.name AS name
                        FROM
                            major m LEFT JOIN major_facility mf on m.id = mf.id_major
                                    LEFT JOIN department_facility df on mf.id_department_facility = df.id
                        WHERE (:#{#request.departmentId} IS NULL OR m.id_department = :#{#request.departmentId})
                             AND (:#{#request.facilityId} IS NULL OR df.id_facility = :#{#request.facilityId})
                    """,
            nativeQuery = true
    )
    List<CMMajorOptionsResponse> getAllMajorByDepartmentId(MajorSearchRequest request);

}
