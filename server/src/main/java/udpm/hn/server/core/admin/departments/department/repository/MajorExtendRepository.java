package udpm.hn.server.core.admin.departments.department.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.departments.department.model.request.FindMajorRequest;
import udpm.hn.server.core.admin.departments.department.model.response.MajorResponse;
import udpm.hn.server.entity.Major;
import udpm.hn.server.repository.MajorRepository;

import java.util.Optional;

@Repository
public interface MajorExtendRepository extends MajorRepository {

    @Query(
            value = """
                        SELECT
                            ROW_NUMBER() OVER (ORDER BY mj.id DESC) AS orderNumber,
                            mj.id AS id,
                            mj.name AS majorName,
                            mj.code AS majorCode, 
                            mj.status AS majorStatus,
                            mj.created_date AS createdDate
                        FROM
                            major mj
                        WHERE
                            mj.id_department = :id
                            AND (:#{#req.majorName} IS NULL
                                OR mj.name LIKE %:#{#req.majorName}%)
                            AND (:#{#req.majorCode} IS NULL
                                OR mj.code LIKE %:#{#req.majorCode}%)
                    """,
            countQuery = """
                        SELECT
                            COUNT(mj.id)
                        FROM
                            major mj
                        WHERE
                            mj.id_department = :id
                            AND (:#{#req.majorName} IS NULL
                                OR mj.name LIKE %:#{#req.majorName}%)
                            AND (:#{#req.majorCode} IS NULL
                                OR mj.code LIKE %:#{#req.majorCode}%)
                    """,
            nativeQuery = true
    )
    Page<MajorResponse> getAllMajorByDepartmentIdFilter(String id, Pageable pageable, @Param("req") FindMajorRequest req);

    Optional<Major> findMajorByNameAndDepartmentId(String name, String departmentId);

}
