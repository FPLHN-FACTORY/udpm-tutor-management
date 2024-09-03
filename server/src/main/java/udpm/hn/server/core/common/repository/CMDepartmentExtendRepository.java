package udpm.hn.server.core.common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.common.model.request.CMOptionsFilterRequest;
import udpm.hn.server.core.common.model.response.CMOptionsResponse;
import udpm.hn.server.repository.DepartmentRepository;

import java.util.List;

@Repository
public interface CMDepartmentExtendRepository extends DepartmentRepository {

    @Query(
            value = """
                    SELECT
                        d.id as id,
                        d.name as name
                    FROM
                        department d
                    WHERE
                        :#{#request.query} is null or d.name like CONCAT('%', :#{#request.query}, '%')
                    """,
            nativeQuery = true
    )
    List<CMOptionsResponse> getAllDepartment(CMOptionsFilterRequest request);

}
