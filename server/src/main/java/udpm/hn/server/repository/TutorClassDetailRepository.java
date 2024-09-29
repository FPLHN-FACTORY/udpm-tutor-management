package udpm.hn.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.headsubject.plan.model.response.TutorClassDetailResponse;
import udpm.hn.server.entity.TutorClass;
import udpm.hn.server.entity.TutorClassDetail;

import java.util.List;

@Repository
public interface TutorClassDetailRepository extends JpaRepository<TutorClassDetail, String> {

    List<TutorClassDetail> findByTutorClass(TutorClass tutorClass);

    @Query(value = """
            SELECT
                ROW_NUMBER() OVER(ORDER BY tcd.id DESC) AS orderNumber,
                tcd.code AS tutorClassCode,
                tcd.number_of_lectures AS numberOfLectures,
                tcd.start_date AS startTime,
                tcd.end_date AS endTime,
                CONCAT(st.staff_Code, ' - ', st.name) AS teacher,
                CONCAT(sd.student_code, ' - ', sd.student_name) AS student
            FROM
                tutor_class_detail tcd
            LEFT JOIN tutor_class tc ON tc.id = tcd.tutor_class_id
            LEFT JOIN subject sj ON sj.id = tc.subject_id
            LEFT JOIN staff st ON st.id = tcd.teacher_conduct_id
            LEFT JOIN student_tutor sd ON sd.id = tcd.student_conduct_id
            WHERE
                tcd.tutor_class_id = :tutorClassId
            """,
            countQuery = """
               SELECT
                ROW_NUMBER() OVER(ORDER BY tcd.id DESC) AS orderNumber,
                tcd.code AS tutorClassCode,
                tcd.number_of_lectures AS numberOfLectures,
                tcd.start_date AS startTime,
                tcd.end_date AS endTime,
                CONCAT(st.staff_Code, ' - ', st.name) AS teacher,
                CONCAT(sd.student_code, ' - ', sd.student_name) AS student
            FROM
                tutor_class_detail tcd
            LEFT JOIN tutor_class tc ON tc.id = tcd.tutor_class_id
            LEFT JOIN subject sj ON sj.id = tc.subject_id
            LEFT JOIN staff st ON st.id = tcd.teacher_conduct_id
            LEFT JOIN student_tutor sd ON sd.id = tcd.student_conduct_id
            WHERE
                tcd.tutor_class_id = :tutorClassId
            """,
            nativeQuery = true)
    Page<TutorClassDetailResponse> getTutorClassDetailByTutorClassId(Pageable pageable, String tutorClassId);

}
