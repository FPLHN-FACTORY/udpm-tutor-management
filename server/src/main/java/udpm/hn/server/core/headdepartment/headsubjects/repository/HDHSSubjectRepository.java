package udpm.hn.server.core.headdepartment.headsubjects.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.subject.model.response.SubjectResponse;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.SubjectByHeadSubjectRequest;
import udpm.hn.server.core.headdepartment.headsubjects.model.response.SubjectAssignResponse;
import udpm.hn.server.repository.SubjectRepository;

@Repository
public interface HDHSSubjectRepository extends SubjectRepository {

    @Query(
            value = """
                    SELECT
                        ROW_NUMBER() OVER (ORDER BY s.id DESC ) as orderNumber,
                        s.id as id,
                        s.subject_code as subjectCode,
                        s.name as subjectName,
                        s.subject_type as subjectType
                    FROM
                        subject s
                    LEFT JOIN head_subject_by_semester hsbs ON hsbs.id_subject = s.id
                    WHERE
                        hsbs.id_staff = :#{#request.headSubjectId} AND hsbs.id_semester = :#{#request.currentSemesterId}
                    """,
            countQuery = """
                    SELECT COUNT(DISTINCT s.id)
                    FROM
                        subject s
                    LEFT JOIN head_subject_by_semester hsbs ON hsbs.id_subject = s.id
                    WHERE
                        hsbs.id_staff = :#{#request.headSubjectId} AND hsbs.id_semester = :#{#request.currentSemesterId}
                    """,
            nativeQuery = true
    )
    Page<SubjectResponse> getSubjectByHeadSubject(Pageable pageable, SubjectByHeadSubjectRequest request);

    @Query(
            value = """
                    SELECT
                                 s.id AS id,
                                 s.subject_code AS subjectCode,
                                 s.name AS subjectName,
                                 s.subject_type AS subjectType,
                                 MAX(IF((hsbs.id_staff = :#{#request.headSubjectId}
                                         AND hsbs.id_semester = :#{#request.currentSemesterId}), 1, 0)) AS isAssigned,
                                 ROW_NUMBER() OVER (ORDER BY s.id DESC) AS orderNumber
                             FROM
                                 subject s
                             LEFT JOIN head_subject_by_semester hsbs ON
                                 s.id = hsbs.id_subject
                             LEFT JOIN staff st ON
                                 hsbs.id_staff = st.id
                             LEFT JOIN department d ON
                                 s.id_department = d.id
                             LEFT JOIN department_facility df ON
                                 d.id = df.id_department
                             LEFT JOIN facility f ON
                                 f.id = df.id_facility
                             WHERE
                             	d.code = :#{#request.departmentCode}
                                 AND f.code = :#{#request.facilityCode}
                                 AND (:#{#request.q} IS NULL OR (
                                     s.subject_code LIKE CONCAT('%',:#{#request.q},'%')
                                 OR s.name LIKE CONCAT('%',:#{#request.q},'%')
                                 ))
                             GROUP BY
                                 s.id,
                                 s.subject_code,
                                 s.name,
                                 s.subject_type
                             ORDER BY
                                 s.id DESC
                    """,
            countQuery = """
                       SELECT
                                 s.id AS id,
                                 s.subject_code AS subjectCode,
                                 s.name AS subjectName,
                                 s.subject_type AS subjectType,
                                 MAX(IF((hsbs.id_staff = :#{#request.headSubjectId}
                                         AND hsbs.id_semester = :#{#request.currentSemesterId}), 1, 0)) AS isAssigned,
                                 ROW_NUMBER() OVER (ORDER BY s.id DESC) AS orderNumber
                             FROM
                                 subject s
                             LEFT JOIN head_subject_by_semester hsbs ON
                                 s.id = hsbs.id_subject
                             LEFT JOIN staff st ON
                                 hsbs.id_staff = st.id
                             LEFT JOIN department d ON
                                 s.id_department = d.id
                             LEFT JOIN department_facility df ON
                                 d.id = df.id_department
                             LEFT JOIN facility f ON
                                 f.id = df.id_facility
                             WHERE
                             	d.code = :#{#request.departmentCode}
                                 AND f.code = :#{#request.facilityCode}
                                 AND (:#{#request.q} IS NULL OR (
                                     s.subject_code LIKE CONCAT('%',:#{#request.q},'%')
                                 OR s.name LIKE CONCAT('%',:#{#request.q},'%')
                                 ))
                             GROUP BY
                                 s.id,
                                 s.subject_code,
                                 s.name,
                                 s.subject_type
                             ORDER BY
                                 s.id DESC
                    """,
            nativeQuery = true
    )
    Page<SubjectAssignResponse> getSubjectAssign(Pageable pageable, SubjectByHeadSubjectRequest request);

}
