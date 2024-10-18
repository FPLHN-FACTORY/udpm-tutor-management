package udpm.hn.server.core.teacher.tutorclass.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCTutorClassListRequest;
import udpm.hn.server.core.teacher.tutorclass.model.response.TCTCTutorClassResponse;
import udpm.hn.server.repository.TutorClassRepository;

@Repository
public interface TCTCTutorClassExtendRepository extends TutorClassRepository {

    @Query(
            value =
                """
                SELECT
                    ROW_NUMBER() OVER(ORDER BY tcd.created_date DESC) AS orderNumber,
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
                GROUP BY tcd.id, tcd.code, st.student_name, s.name, tcd.created_date
                """,
            nativeQuery = true
    )
    Page<TCTCTutorClassResponse> getTutorClassByTeacher(Pageable pageable, TCTCTutorClassListRequest request);

}
