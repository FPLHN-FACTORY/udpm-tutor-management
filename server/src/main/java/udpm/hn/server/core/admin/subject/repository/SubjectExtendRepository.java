package udpm.hn.server.core.admin.subject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.subject.model.request.SubjectRequest;
import udpm.hn.server.core.admin.subject.model.response.DetailSubjectResponse;
import udpm.hn.server.core.admin.subject.model.response.SubjectResponse;
import udpm.hn.server.entity.Subject;
import udpm.hn.server.repository.SubjectRepository;

import java.util.Optional;

@Repository
public interface SubjectExtendRepository extends SubjectRepository {

    @Query(
            value = """
                    SELECT
                        ROW_NUMBER() OVER (ORDER BY s.id DESC ) as orderNumber,
                        s.id as id,
                        s.subject_code as subjectCode,
                        s.name as subjectName,
                        d.name as departmentName,
                        s.subject_type as subjectType,
                        s.created_date as createdDate
                    FROM
                        subject s
                    LEFT JOIN
                        department d on s.id_department = d.id
                    WHERE
                        (:#{#request.subjectCode} IS NULL OR s.subject_code LIKE CONCAT('%',:#{#request.subjectCode},'%'))
                        AND (:#{#request.subjectName} IS NULL OR s.name LIKE CONCAT('%',:#{#request.subjectName},'%'))
                        AND (:#{#request.departmentId} IS NULL OR s.id_department = :#{#request.departmentId})
                        AND (:#{#request.subjectType} IS NULL OR s.subject_type = :#{#request.subjectType})
                        AND (:#{#request.startDate} IS NULL OR s.created_date >= :#{#request.startDate})
                    """,
            countQuery = """
                    SELECT
                        COUNT(DISTINCT s.id)
                    FROM
                        subject s
                    LEFT JOIN
                        department d on s.id_department = d.id
                    WHERE
                        (:#{#request.subjectCode} IS NULL OR s.subject_code LIKE CONCAT('%',:#{#request.subjectCode},'%'))
                        AND (:#{#request.subjectName} IS NULL OR s.name LIKE CONCAT('%',:#{#request.subjectName},'%'))
                        AND (:#{#request.departmentId} IS NULL OR s.id_department = :#{#request.departmentId})
                        AND (:#{#request.subjectType} IS NULL OR s.subject_type = :#{#request.subjectType})
                        AND (:#{#request.startDate} IS NULL OR s.created_date >= :#{#request.startDate})
                    """,
            nativeQuery = true
    )
    Page<SubjectResponse> getAllSubject(Pageable pageable, SubjectRequest request);

    Optional<Subject> findBySubjectCode(String subjectCode);

    @Query(
            value = """
                    SELECT
                        s.id as id,
                        s.subject_code as subjectCode,
                        s.name as subjectName,
                        s.id_department as departmentId,
                        s.subject_type as subjectType,
                        s.created_date as createdDate
                    FROM
                        subject s
                    WHERE
                        s.id = :subjectId
                    """,
            nativeQuery = true
    )
    Optional<DetailSubjectResponse> getDetailSubjectById(String subjectId);

}
