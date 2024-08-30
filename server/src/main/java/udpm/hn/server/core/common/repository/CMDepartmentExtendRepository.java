package udpm.hn.server.core.common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.common.model.response.DepartmentOptionsResponse;
import udpm.hn.server.repository.DepartmentRepository;

import java.util.List;

@Repository
public interface CMDepartmentExtendRepository extends DepartmentRepository {

    @Query(
            value = """
                    SELECT
                        d.id as departmentId,
                        d.name as departmentName
                    FROM
                        department d
                    """,
            nativeQuery = true
    )
    List<DepartmentOptionsResponse> getAllDepartment();

}
