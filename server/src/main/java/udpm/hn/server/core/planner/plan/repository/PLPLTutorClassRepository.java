package udpm.hn.server.core.planner.plan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.planner.plan.model.response.PLPLTutorClassResponse;
import udpm.hn.server.repository.TutorClassRepository;

@Repository
public interface PLPLTutorClassRepository extends TutorClassRepository {

    @Query(value = """
            SELECT
                ROW_NUMBER() OVER(ORDER BY tc.id DESC) AS orderNumber,
                tc.number_of_classes AS numberClasses,
                sj.name AS subjectName,
                tc.id as id,
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
                :planId IS NULL OR pl.id LIKE CONCAT('%', :planId, '%')
            """, countQuery = """
            SELECT
                ROW_NUMBER() OVER(ORDER BY tc.created_date DESC) AS orderNumber,
                tc.number_of_classes AS numberClasses,
                sj.name AS subjectName,
                tc.id as id,
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
                :planId IS NULL OR pl.id LIKE CONCAT('%', :planId, '%')
            """, nativeQuery = true)
    Page<PLPLTutorClassResponse> getTutorClasses(Pageable pageable, String planId);

}
