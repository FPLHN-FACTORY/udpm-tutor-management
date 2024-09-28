package udpm.hn.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.headsubject.model.response.TutorClassDetailResponse;
import udpm.hn.server.entity.TutorClass;
import udpm.hn.server.entity.TutorClassDetail;

import java.util.List;

@Repository
public interface TutorClassDetailRepository extends JpaRepository<TutorClassDetail, String> {

    List<TutorClassDetail> findByTutorClass(TutorClass tutorClass);

    @Query(value = """
            SELECT 
                ROW_NUMBER() OVER(ORDER BY tcd.id DESC) AS orderNumber,
                sj.name AS nameTutorClass,
                CONCAT(s.staff_Code, ' - ', s.name) AS headSubject,
                tcd.number_of_lectures AS numberOfLectures,
                tcd.start_date AS startTime,
                tcd.end_date AS endTime
            FROM 
                tutor_class_detail tcd
            LEFT JOIN tutor_class tc ON tc.id = tcd.tutor_class_id
            LEFT JOIN subject sj ON sj.id = tc.subject_id
            LEFT JOIN head_subject_by_semester hsbs ON hsbs.id_subject = sj.id
            LEFT JOIN staff s ON s.id = hsbs.id_staff
            WHERE 
                tcd.tutor_class_id = :tutorClassId
            """,
            countQuery = """
    SELECT COUNT(*)
    FROM 
        tutor_class_detail tcd
    LEFT JOIN tutor_class tc ON tc.id = tcd.tutor_class_id
    LEFT JOIN subject sj ON sj.id = tc.subject_id
    LEFT JOIN head_subject_by_semester hsbs ON hsbs.id_subject = sj.id
    LEFT JOIN staff s ON s.id = hsbs.id_staff
    WHERE 
        tcd.tutor_class_id = :tutorClassId
    """,
            nativeQuery = true)
    Page<TutorClassDetailResponse> getTutorClassDetailByTutorClassId(Pageable pageable, String tutorClassId);

}
