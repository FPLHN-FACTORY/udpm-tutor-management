package udpm.hn.server.core.admin.departments.departmentfacility.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.departments.departmentfacility.model.response.ListMajorResponse;
import udpm.hn.server.repository.MajorRepository;

import java.util.List;

@Repository
public interface DFMajorExtendRepository extends MajorRepository {

    @Query(
            value = """
                    SELECT
                        m.id AS majorId,
                        m.name AS majorName
                    FROM
                        major m
                    WHERE
                        m.id_department = :departmentId
                    """,
            nativeQuery = true
    )
    List<ListMajorResponse> findAllByDepartmentId(String departmentId);

}
