package udpm.hn.server.core.admin.subject.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.subject.model.response.DepartmentListResponse;
import udpm.hn.server.repository.DepartmentRepository;

import java.util.List;

@Repository
public interface DepartmentSubjectRepository extends DepartmentRepository {

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
    List<DepartmentListResponse> getAllDepartment();

}
