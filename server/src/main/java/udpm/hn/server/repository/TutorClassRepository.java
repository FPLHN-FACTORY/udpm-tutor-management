package udpm.hn.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.subject.model.response.HSTutorClassResponse;
import udpm.hn.server.core.headsubject.model.response.TutorClassResponse;
import udpm.hn.server.core.planner.plan.model.response.PLPLTutorClassResponse;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.entity.Subject;
import udpm.hn.server.entity.TutorClass;
import udpm.hn.server.infrastructure.constant.PlanFormat;

import java.util.Optional;

@Repository
public interface TutorClassRepository extends JpaRepository<TutorClass, String> {
    Optional<TutorClass> findByPlanAndSubject(Plan plan, Subject subject);
    Optional<TutorClass> findBySubjectAndPlanFormat(Subject subject, PlanFormat planFormat);

    @Query("SELECT tc.id AS id, tc.numberOfClasses AS numberOfClasses " +
            "FROM TutorClass tc WHERE tc.id = :id")
    TutorClassResponse getDetailTutorClass(String id);

    @Query(value = """
        SELECT
            ROW_NUMBER() OVER(ORDER BY tc.id DESC) AS orderNumber,
            tc.number_of_classes AS numberClasses,
            sj.name AS subjectName,
            tc.format AS format,
            tc.id as id,
            tc.status as status,
            CONCAT(st.staff_code, ' - ', st.name) AS headSubject
        FROM
            tutor_class tc
        LEFT JOIN plan pl ON
            pl.id = tc.plan_id
        LEFT JOIN block b ON
            b.id = pl.block_id
        LEFT JOIN semester s ON
            s.id = b.semester_id    
        LEFT JOIN subject sj ON
            sj.id = tc.subject_id
        LEFT JOIN department_facility df ON
            df.id = pl.department_facility_id
        LEFT JOIN department d ON
            d.id = df.id_department
        LEFT JOIN facility f ON
            f.id = df.id_facility
        LEFT JOIN head_subject_by_semester hsbs ON
            hsbs.id_subject = sj.id AND hsbs.id_semester = s.id AND hsbs.id_facility = f.id
        LEFT JOIN staff st ON
            st.id = hsbs.id_staff                       
        WHERE
            (:planId IS NULL OR pl.id LIKE CONCAT('%', :planId, '%'))
            AND (:departmentCode IS NULL OR d.code LIKE CONCAT('%', :departmentCode, '%'))
            AND (:facilityCode IS NULL OR f.code LIKE CONCAT('%', :facilityCode, '%'))
            AND (:semesterId IS NULL OR s.id = :semesterId)
           /* AND tc.status != 1 */
        """, countQuery = """
        SELECT
            ROW_NUMBER() OVER(ORDER BY tc.created_date DESC) AS orderNumber,
            tc.number_of_classes AS numberClasses,
            sj.name AS subjectName,
            tc.format AS format,
            tc.id as id,
            tc.status as status,
            CONCAT(st.staff_code, ' - ', st.name) AS headSubject
        FROM
            tutor_class tc
        LEFT JOIN plan pl ON
            pl.id = tc.plan_id
        LEFT JOIN block b ON
            b.id = pl.block_id
        LEFT JOIN semester s ON
            s.id = b.semester_id     
        LEFT JOIN subject sj ON
            sj.id = tc.subject_id
        LEFT JOIN department_facility df ON
            df.id = pl.department_facility_id
        LEFT JOIN department d ON
            d.id = df.id_department
        LEFT JOIN facility f ON
            f.id = df.id_facility
        LEFT JOIN head_subject_by_semester hsbs ON
            hsbs.id_subject = sj.id AND hsbs.id_semester = s.id AND hsbs.id_facility = f.id
        LEFT JOIN staff st ON
            st.id = hsbs.id_staff               
        WHERE
            (:planId IS NULL OR pl.id LIKE CONCAT('%', :planId, '%'))
            AND (:departmentCode IS NULL OR d.code LIKE CONCAT('%', :departmentCode, '%'))
            AND (:facilityCode IS NULL OR f.code LIKE CONCAT('%', :facilityCode, '%'))
            AND (:semesterId IS NULL OR s.id = :semesterId)
             /* AND tc.status != 1 */
        """, nativeQuery = true)
    Page<HSTutorClassResponse> getTutorClasses(Pageable pageable, String planId, String departmentCode, String facilityCode, String semesterId);

}
