package udpm.hn.server.core.headdepartment.headsubjects.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.HeadSubjectRequest;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.HeadSubjectSearchRequest;
import udpm.hn.server.core.headdepartment.headsubjects.model.response.HeadSubjectResponse;
import udpm.hn.server.core.headdepartment.headsubjects.model.response.HeadSubjectSearchResponse;
import udpm.hn.server.entity.HeadSubjectBySemester;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.repository.HeadSubjectBySemesterRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HDHSHeadSubjectBySemesterRepository extends HeadSubjectBySemesterRepository {

    @Query(
            value = """
                    SELECT
                        ROW_NUMBER() OVER (ORDER BY s.id DESC ) as orderNumber,
                        s.id as id,
                        s.staff_code as staffCode,
                        s.name as staffName,
                        s.email_fpt as emailFPT,
                        s.email_fe as emailFE,
                        IF(MAX(hsbs.id) IS NOT NULL, TRUE, FALSE) as isAssigned,
                        COUNT(DISTINCT hsbs.id) as assignedCount
                    FROM
                        staff s
                    LEFT JOIN staff_major_facility smf ON s.id = smf.id_staff
                    LEFT JOIN major_facility mf ON mf.id = smf.id_major_facility
                    LEFT JOIN department_facility df ON mf.id_department_facility = df.id
                    LEFT JOIN facility f ON f.id = df.id_facility
                    LEFT JOIN department d ON d.id = df.id_department
                    LEFT JOIN head_subject_by_semester hsbs ON hsbs.id_staff = s.id AND hsbs.id_semester = :#{#request.currentSemesterId}
                    LEFT JOIN semester se ON se.id = hsbs.id_semester
                    LEFT JOIN staff_role sr ON sr.id_staff = s.id
                    LEFT JOIN role r ON r.id = sr.id_role
                    WHERE
                        r.code = :#{#request.headSubjectRoleCode}
                        AND d.code = :#{#request.currentDepartmentCode}
                        AND f.code = :#{#request.currentFacilityCode}
                        AND ((:#{#request.q} IS NULL OR s.staff_code LIKE CONCAT('%',:#{#request.q},'%')) OR (:#{#request.q} IS NULL OR s.name LIKE CONCAT('%',:#{#request.q},'%')) )
                    GROUP BY s.id, s.staff_code, s.name, s.email_fpt, s.email_fe
                    """,
            countQuery = """
                    SELECT COUNT(DISTINCT s.id)
                    FROM
                        staff s
                    LEFT JOIN staff_major_facility smf ON s.id = smf.id_staff
                    LEFT JOIN major_facility mf ON mf.id = smf.id_major_facility
                    LEFT JOIN department_facility df ON mf.id_department_facility = df.id
                    LEFT JOIN facility f ON f.id = df.id_facility
                    LEFT JOIN department d ON d.id = df.id_department
                    LEFT JOIN head_subject_by_semester hsbs ON hsbs.id_staff = s.id AND hsbs.id_semester = :#{#request.currentSemesterId}
                    LEFT JOIN semester se ON se.id = hsbs.id_semester
                    LEFT JOIN staff_role sr ON sr.id_staff = s.id
                    LEFT JOIN role r ON r.id = sr.id_role
                    WHERE
                        r.code = :#{#request.headSubjectRoleCode}
                        AND d.code = :#{#request.currentDepartmentCode}
                        AND f.code = :#{#request.currentFacilityCode}
                        AND ((:#{#request.q} IS NULL OR s.staff_code LIKE CONCAT('%',:#{#request.q},'%')) OR (:#{#request.q} IS NULL OR s.name LIKE CONCAT('%',:#{#request.q},'%')) )
                    """,
            nativeQuery = true
    )
    Page<HeadSubjectResponse> getAllHeadSubjectsBySemester(Pageable pageable, HeadSubjectRequest request);

    Optional<HeadSubjectBySemester> findBySemester_IdAndSubject_IdAndFacility_Id(
            String semesterId,
            String subjectId,
            String facilityId
    );

    List<HeadSubjectBySemester> findBySemester_IdAndFacility_IdAndStaff_Id(
            String semesterId,
            String facilityId,
            String staffId
    );

    @Query(
            value = """
                    SELECT
                        s.id as code,
                        CONCAT(s.staff_code, ' - ', s.name) as staffInfo
                    FROM
                        staff s
                    LEFT JOIN staff_major_facility smf ON s.id = smf.id_staff
                    LEFT JOIN major_facility mf ON mf.id = smf.id_major_facility
                    LEFT JOIN department_facility df ON mf.id_department_facility = df.id
                    LEFT JOIN department d ON d.id = df.id_department
                    LEFT JOIN head_subject_by_semester hsbs ON hsbs.id_staff = s.id AND hsbs.id_semester = :#{#request.currentSemesterId}
                    LEFT JOIN semester se ON se.id = hsbs.id_semester
                    LEFT JOIN staff_role sr ON sr.id_staff = s.id
                    LEFT JOIN role r ON r.id = sr.id_role
                    WHERE
                        r.code = :currentRoleName
                        AND r.id_facility = :#{#request.currentFacilityId}
                        AND s.id != :#{#request.currentUserId}
                        AND s.id != :#{#request.headSubjectId}
                        AND d.code = :#{#request.currentDepartmentCode}
                        AND (:#{#request.q} IS NULL OR (s.staff_code LIKE CONCAT('%',:#{#request.q},'%') OR s.name LIKE CONCAT('%',:#{#request.q},'%')))
                    GROUP BY s.id, s.staff_code, s.name, s.email_fpt, s.email_fe
                    """,
            nativeQuery = true
    )
    List<HeadSubjectSearchResponse> getHeadSubjects(
            HeadSubjectSearchRequest request,
            String currentRoleName
    );

    List<HeadSubjectBySemester> findBySemester(Semester semester);

}
