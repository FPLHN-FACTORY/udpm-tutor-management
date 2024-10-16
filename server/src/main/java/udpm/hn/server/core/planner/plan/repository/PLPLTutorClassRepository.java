package udpm.hn.server.core.planner.plan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.planner.plan.model.request.PLPLSubjectListRequest;
import udpm.hn.server.core.planner.plan.model.response.PLPLTutorClassResponse;
import udpm.hn.server.repository.TutorClassRepository;

@Repository
public interface PLPLTutorClassRepository extends TutorClassRepository {

    @Query(value = """
            SELECT
                 ROW_NUMBER() OVER(ORDER BY hsbs.id DESC) AS orderNumber,
                 tc.id,
                 sj.id AS subjectId,
                 sj.code AS subjectCode,
                 sj.name AS subjectName,
                 CONCAT(st.staff_code, ' - ', st.name) AS headSubject,
                 COALESCE(tcd.numberClasses, 0) AS numberClasses,
                 COALESCE(tc.number_of_lectures, 0) AS numberLectures,
                 tc.format AS format
            FROM
                head_subject_by_semester hsbs
            LEFT JOIN staff st ON
                st.id = hsbs.id_staff
            LEFT JOIN subject sj ON
                sj.id = hsbs.id_subject
            LEFT JOIN tutor_class tc ON
                tc.subject_id = hsbs.id_subject
            LEFT JOIN (
                SELECT tcd.tutor_class_id, COUNT(*) AS numberClasses
                FROM tutor_class_detail tcd
                GROUP BY tcd.tutor_class_id
            ) tcd ON tcd.tutor_class_id = tc.id
            WHERE
                tc.plan_id = :#{#request.planId}
                AND (:#{#request.semesterId} IS NULL OR hsbs.id_semester LIKE :#{#request.semesterId})
                AND (:#{#request.facilityId} IS NULL OR hsbs.id_facility LIKE :#{#request.facilityId})
            """, countQuery = """
           SELECT
                 ROW_NUMBER() OVER(ORDER BY hsbs.id DESC) AS orderNumber,
                 tc.id,
                 sj.id AS subjectId,
                 sj.code AS subjectCode,
                 sj.name AS subjectName,
                 CONCAT(st.staff_code, ' - ', st.name) AS headSubject,
                 COALESCE(tcd.numberClasses, 0) AS numberClasses,
                 COALESCE(tc.number_of_lectures, 0) AS numberLectures,
                 tc.format AS format
            FROM
                head_subject_by_semester hsbs
            LEFT JOIN staff st ON
                st.id = hsbs.id_staff
            LEFT JOIN subject sj ON
                sj.id = hsbs.id_subject
            LEFT JOIN tutor_class tc ON
                tc.subject_id = hsbs.id_subject
            LEFT JOIN (
                SELECT tcd.tutor_class_id, COUNT(*) AS numberClasses
                FROM tutor_class_detail tcd
                GROUP BY tcd.tutor_class_id
            ) tcd ON tcd.tutor_class_id = tc.id
            WHERE
                tc.plan_id = :#{#request.planId}
                AND (:#{#request.semesterId} IS NULL OR hsbs.id_semester LIKE :#{#request.semesterId})
                AND (:#{#request.facilityId} IS NULL OR hsbs.id_facility LIKE :#{#request.facilityId})
            """, nativeQuery = true)
    Page<PLPLTutorClassResponse> getTutorClasses(Pageable pageable, PLPLSubjectListRequest request);

    @Query(value = """
            SELECT tc.id AS id, tc.number_of_classes AS numberOfClasses,CONCAT(sj.subject_code,  ' - ', sj.name) AS subjectName
            FROM tutor_class tc
            LEFT JOIN subject sj ON
                sj.id = tc.subject_id
            WHERE tc.id = :id
        """, nativeQuery = true)
    PLPLTutorClassResponse getDetailTutorClass(String id);

}
