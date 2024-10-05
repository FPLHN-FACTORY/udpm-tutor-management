package udpm.hn.server.core.headsubject.plan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.subject.model.response.HSTutorClassResponse;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLSubjectListRequest;
import udpm.hn.server.core.headsubject.plan.model.response.HSPLTutorClassResponse;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.entity.Subject;
import udpm.hn.server.entity.TutorClass;
import udpm.hn.server.repository.TutorClassRepository;

import java.util.Optional;

@Repository
public interface HSPLTutorClassRepository extends TutorClassRepository {

    Optional<TutorClass> findByPlanAndSubject(Plan plan, Subject subject);

    @Query(value = """
            SELECT tc.id AS id,
            CONCAT(sj.subject_code,  ' - ', sj.name) AS subjectName,
            COALESCE(tcd.numberClasses, 0) AS numberClasses,
            COALESCE(tc.number_of_lectures, 0) AS numberLectures,
            tc.format AS format
            FROM tutor_class tc
            LEFT JOIN subject sj ON
                sj.id = tc.subject_id
            LEFT JOIN (
                SELECT tcd.tutor_class_id, COUNT(*) AS numberClasses
                FROM tutor_class_detail tcd
                GROUP BY tcd.tutor_class_id
            ) tcd ON tcd.tutor_class_id = tc.id    
            WHERE tc.id = :id
        """, nativeQuery = true)
    HSPLTutorClassResponse getDetailTutorClass(String id);

    @Query(value = """
            SELECT
                 ROW_NUMBER() OVER(ORDER BY hsbs.id DESC) AS orderNumber,
                 tc.id,
                 sj.id AS subjectId,
                 sj.code AS subjectCode,
                 sj.name AS subjectName,
                 CONCAT(st.staff_code, ' - ', st.name) AS headSubject,
                 COALESCE(tc.tutor_class_status, 0) AS status,
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
                tc.subject_id = hsbs.id_subject AND tc.plan_id = :#{#request.planId}
            LEFT JOIN (
                SELECT tcd.tutor_class_id, COUNT(*) AS numberClasses
                FROM tutor_class_detail tcd
                GROUP BY tcd.tutor_class_id
            ) tcd ON tcd.tutor_class_id = tc.id
            WHERE
                (:#{#request.userId} IS NULL OR hsbs.id_staff LIKE :#{#request.userId})
                AND (:#{#request.semesterId} IS NULL OR hsbs.id_semester LIKE :#{#request.semesterId})
                AND (:#{#request.facilityId} IS NULL OR hsbs.id_facility LIKE :#{#request.facilityId})
        """, countQuery = """
            SELECT
                 ROW_NUMBER() OVER(ORDER BY hsbs.created_date DESC) AS orderNumber,
                  tc.id,
                 sj.id AS subjectId,
                 sj.name AS subjectName,
                 CONCAT(st.staff_code, ' - ', st.name) AS headSubject,
                 COALESCE(tc.tutor_class_status, 0) AS status,
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
                tc.subject_id = hsbs.id_subject AND tc.plan_id = :#{#request.planId}
            LEFT JOIN (
                SELECT tcd.tutor_class_id, COUNT(*) AS numberClasses
                FROM tutor_class_detail tcd
                GROUP BY tcd.tutor_class_id
            ) tcd ON tcd.tutor_class_id = tc.id
            WHERE
                (:#{#request.userId} IS NULL OR hsbs.id_staff LIKE :#{#request.userId})
                AND (:#{#request.semesterId} IS NULL OR hsbs.id_semester LIKE :#{#request.semesterId})
                AND (:#{#request.facilityId} IS NULL OR hsbs.id_facility LIKE :#{#request.facilityId})
        """, nativeQuery = true)
    Page<HSTutorClassResponse> getTutorClasses(Pageable pageable, HSPLSubjectListRequest request);

}
