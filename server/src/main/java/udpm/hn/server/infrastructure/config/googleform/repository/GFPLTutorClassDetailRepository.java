package udpm.hn.server.infrastructure.config.googleform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.planner.plan.model.request.PLPLTutorClassDetailRequest;
import udpm.hn.server.core.planner.plan.model.response.PLPLTutorClassDetailResponse;
import udpm.hn.server.repository.TutorClassDetailRepository;

import java.util.List;

@Repository
public interface GFPLTutorClassDetailRepository extends TutorClassDetailRepository {

    @Query(value = """
            SELECT
                ROW_NUMBER() OVER(ORDER BY tcd.code) AS orderNumber,
                tcd.id,
                tcd.code AS tutorClassCode,
                tcd.student_conduct_id AS studentTutor,
                tcd.teacher_conduct_id AS teacherTutor,
                tcd.room AS room,
                tcd.number_of_lectures AS numberOfLectures,
                tcd.start_date AS startTime,
                tcd.end_date AS endTime,
                tcd.default_shift AS shift,
                tcd.link AS link,
                CONCAT(st.staff_Code, ' - ', st.name) AS teacher,
                CONCAT(sd.student_code, ' - ', sd.student_name) AS student
            FROM
                tutor_class_detail tcd
            LEFT JOIN tutor_class tc ON tc.id = tcd.tutor_class_id
            LEFT JOIN plan pl ON pl.id = tc.plan_id
            LEFT JOIN subject sj ON sj.id = tc.subject_id
            LEFT JOIN head_subject_by_semester hsbs ON hsbs.id_subject = sj.id
            LEFT JOIN staff st ON st.id = tcd.teacher_conduct_id
            LEFT JOIN student_tutor sd ON sd.id = tcd.student_conduct_id
            WHERE
                pl.id = :#{#request.planId}
                AND (:#{#request.teacherId} IS NULL OR tcd.teacher_conduct_id LIKE CONCAT('%', :#{#request.teacherId}, '%'))
                AND (:#{#request.query} IS NULL OR tcd.code LIKE CONCAT('%', :#{#request.query}, '%'))
                AND (:#{#request.semesterId} IS NULL OR hsbs.id_semester LIKE :#{#request.semesterId})
                AND (:#{#request.facilityId} IS NULL OR hsbs.id_facility LIKE :#{#request.facilityId})
            ORDER BY tcd.code
            """,
            countQuery = """
             SELECT
                ROW_NUMBER() OVER(ORDER BY tcd.code) AS orderNumber,
                tcd.id,
                tcd.code AS tutorClassCode,
                tcd.student_conduct_id AS studentTutor,
                tcd.teacher_conduct_id AS teacherTutor,
                tcd.room AS room,
                tcd.link AS link,
                tcd.number_of_lectures AS numberOfLectures,
                tcd.start_date AS startTime,
                tcd.end_date AS endTime,
                tcd.default_shift AS shift,
                CONCAT(st.staff_Code, ' - ', st.name) AS teacher,
                CONCAT(sd.student_code, ' - ', sd.student_name) AS student
            FROM
                tutor_class_detail tcd
            LEFT JOIN tutor_class tc ON tc.id = tcd.tutor_class_id
            LEFT JOIN plan pl ON pl.id = tc.plan_id
            LEFT JOIN subject sj ON sj.id = tc.subject_id
            LEFT JOIN head_subject_by_semester hsbs ON hsbs.id_subject = sj.id
            LEFT JOIN staff st ON st.id = tcd.teacher_conduct_id
            LEFT JOIN student_tutor sd ON sd.id = tcd.student_conduct_id
            WHERE
                pl.id = :#{#request.planId}
                AND (:#{#request.teacherId} IS NULL OR tcd.teacher_conduct_id LIKE CONCAT('%', :#{#request.teacherId}, '%'))
                AND (:#{#request.query} IS NULL OR tcd.code LIKE CONCAT('%', :#{#request.query}, '%'))
                AND (:#{#request.semesterId} IS NULL OR hsbs.id_semester LIKE :#{#request.semesterId})
                AND (:#{#request.facilityId} IS NULL OR hsbs.id_facility LIKE :#{#request.facilityId})
            ORDER BY tcd.code
            """,
            nativeQuery = true)
    Page<PLPLTutorClassDetailResponse> getTutorClassDetailByTutorClassId(Pageable pageable, PLPLTutorClassDetailRequest request);

    @Query(value = """
            SELECT
                CONCAT(tcd.code, ' ( ', sj.name, ' )') AS tutorClassCode,
                tcd.link AS link
            FROM
                tutor_class_detail tcd
            LEFT JOIN tutor_class tc ON tc.id = tcd.tutor_class_id
            LEFT JOIN plan pl ON pl.id = tc.plan_id
            LEFT JOIN subject sj ON sj.id = tc.subject_id
            LEFT JOIN head_subject_by_semester hsbs ON hsbs.id_subject = sj.id
            LEFT JOIN staff st ON st.id = tcd.teacher_conduct_id
            LEFT JOIN student_tutor sd ON sd.id = tcd.student_conduct_id
            WHERE
                pl.id = :planId
            ORDER BY tcd.code
            """,
            nativeQuery = true)
    List<PLPLTutorClassDetailResponse> getDataTutorClassDetail(String planId);

}
