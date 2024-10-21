package udpm.hn.server.core.teacher.tutorclass.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.teacher.tutorclass.model.response.TCTCTutorClassDetailResponse;
import udpm.hn.server.repository.TutorClassDetailRepository;

import java.util.List;

@Repository
public interface TCTCTutorClassDetailExtendRepository extends TutorClassDetailRepository {

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
                        CONCAT(st.student_name, ' - ', st.student_code) AS student,
                        s.name AS subjectName,
                        tc.format AS format,
                        sub_csj.totalStudent AS totalStudent
                    FROM tutor_class_detail tcd
                    LEFT JOIN tutor_class tc on tc.id = tcd.tutor_class_id
                    LEFT JOIN student_tutor st on tcd.student_conduct_id = st.id
                    LEFT JOIN subject s on s.id = tc.subject_id
                    LEFT JOIN class_student_joined csj on tc.id = csj.tutor_class_detail_id
                    LEFT JOIN (
                        SELECT csj.tutor_class_detail_id, COUNT(*) AS totalStudent
                        FROM class_student_joined csj
                        GROUP BY csj.tutor_class_detail_id
                    ) sub_csj ON sub_csj.tutor_class_detail_id = tcd.id
                    WHERE tcd.id = :tutorClassDetailId
                    """,
            countQuery = """
                    SELECT
                        COUNT(DISTINCT tcd.id)
                    FROM tutor_class_detail tcd
                    LEFT JOIN tutor_class tc on tc.id = tcd.tutor_class_id
                    LEFT JOIN student_tutor st on tcd.student_conduct_id = st.id
                    LEFT JOIN subject s on s.id = tc.subject_id
                    LEFT JOIN class_student_joined csj on tc.id = csj.tutor_class_detail_id
                    LEFT JOIN (
                        SELECT csj.tutor_class_detail_id, COUNT(*) AS totalStudent
                        FROM class_student_joined csj
                        GROUP BY csj.tutor_class_detail_id
                    ) sub_csj ON sub_csj.tutor_class_detail_id = tcd.id
                    WHERE tcd.id = :tutorClassDetailId
                    """,
            nativeQuery = true
    )
    TCTCTutorClassDetailResponse getTutorClassDetail(String tutorClassDetailId);

}
