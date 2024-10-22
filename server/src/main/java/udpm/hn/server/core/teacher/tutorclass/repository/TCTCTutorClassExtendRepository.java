package udpm.hn.server.core.teacher.tutorclass.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCCurrentPlanRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCTutorClassListRequest;
import udpm.hn.server.core.teacher.tutorclass.model.response.TCTCPlanResponse;
import udpm.hn.server.core.teacher.tutorclass.model.response.TCTCTutorClassResponse;
import udpm.hn.server.repository.TutorClassRepository;

import java.util.List;

@Repository
public interface TCTCTutorClassExtendRepository extends TutorClassRepository {

    @Query(
            value =
                """
                SELECT
                    ROW_NUMBER() OVER(ORDER BY tcd.code DESC) AS orderNumber,
                    tcd.id AS id,
                    tcd.code AS classCode,
                    tcd.start_date AS startTime,
                    tcd.end_date AS endTime,
                    tcd.default_shift AS shift,
                    st.student_name AS studentName,
                    s.name AS subjectName,
                    COUNT(*) AS totalStudent
                FROM tutor_class_detail tcd LEFT JOIN tutor_class tc on tc.id = tcd.tutor_class_id
                                            LEFT JOIN student_tutor st on tcd.student_conduct_id = st.id
                                            LEFT JOIN subject s on s.id = tc.subject_id
                                            LEFT JOIN class_student_joined csj on tc.id = csj.tutor_class_detail_id
                WHERE (:#{#request.classCode} IS NULL OR tcd.code LIKE CONCAT('%',:#{#request.classCode},'%'))
                    AND (:#{#request.subjectId} IS NULL OR s.id LIKE CONCAT('%',:#{#request.subjectId},'%'))
                    AND (tcd.teacher_conduct_id = :#{#request.teacherId})
                    AND tc.plan_id = :#{#request.planId}
                GROUP BY tcd.id, tcd.code, st.student_name, s.name, tcd.created_date
                ORDER BY
                    tcd.code DESC
                """,
            countQuery = """
                    SELECT
                        COUNT(DISTINCT tcd.id)
                    FROM tutor_class_detail tcd LEFT JOIN tutor_class tc on tc.id = tcd.tutor_class_id
                                            LEFT JOIN student_tutor st on tcd.student_conduct_id = st.id
                                            LEFT JOIN subject s on s.id = tc.subject_id
                                            LEFT JOIN class_student_joined csj on tc.id = csj.tutor_class_detail_id
                WHERE (:#{#request.classCode} IS NULL OR tcd.code LIKE CONCAT('%',:#{#request.classCode},'%'))
                    AND (:#{#request.subjectId} IS NULL OR s.id LIKE CONCAT('%',:#{#request.subjectId},'%'))
                    AND (tcd.teacher_conduct_id = :#{#request.teacherId})
                    AND tc.plan_id = :#{#request.planId}
                GROUP BY tcd.id, tcd.code, st.student_name, s.name, tcd.created_date
                ORDER BY
                    tcd.created_date DESC
                    """,
            nativeQuery = true
    )
    Page<TCTCTutorClassResponse> getTutorClassByTeacher(Pageable pageable, TCTCTutorClassListRequest request);

    @Query(
            value = """
                SELECT
                    pl.id as id,
                    CONCAT(s.name, ' ', s.year, ' ', b.name) as name,
                    CASE
                        WHEN b.id = :#{#request.blockId} THEN 1
                        ELSE 0
                    END as isCurrent,
                    pl.plan_status as planStatus
                FROM
                    plan pl
                LEFT JOIN block b on pl.block_id = b.id
                LEFT JOIN semester s on s.id = b.semester_id
                LEFT JOIN department_facility df ON df.id = pl.department_facility_id        
                LEFT JOIN facility f ON f.id = df.id_facility
                LEFT JOIN department d ON d.id = df.id_department
                where (:#{#request.facilityCode} IS NULL OR f.code LIKE :#{#request.facilityCode})
                AND (:#{#request.departmentCode} IS NULL OR d.code LIKE :#{#request.departmentCode})
                """,
            nativeQuery = true
    )
    List<TCTCPlanResponse> getPLan(TCTCCurrentPlanRequest request);
}
