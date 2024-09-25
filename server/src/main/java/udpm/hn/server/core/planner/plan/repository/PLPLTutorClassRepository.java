package udpm.hn.server.core.planner.plan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.planner.plan.model.request.PLPLPlanListRequest;
import udpm.hn.server.core.planner.plan.model.response.PLPLPlanListResponse;
import udpm.hn.server.infrastructure.constant.PlanStatus;
import udpm.hn.server.repository.PlanRepository;

@Repository
public interface PLPLTutorClassRepository extends PlanRepository {

    @Query(value = """
            SELECT
                ROW_NUMBER() OVER(ORDER BY pl.created_date DESC) AS orderNumber,
                pl.id AS id,
                CONCAT(s.name, ' ', s.year) AS planName,
                b.name AS blockName,
                d.name AS departmentName,
                f.name AS facilityName,
                COALESCE(tc.numberSubjects, 0) AS numberSubjects,
                pl.plan_status AS status
            FROM
                tutor_class tc
            LEFT JOIN plan pl ON
                pl.id = tc.plan_id
            LEFT JOIN semester s ON
                b.semester_id = s.id
            LEFT JOIN department_facility df ON
                df.id = pl.department_facility_id
            LEFT JOIN facility f ON
                df.id_facility = f.id
            LEFT JOIN department d ON
                d.id = df.id_department
            LEFT JOIN (
                SELECT tc.plan_id, COUNT(*) AS numberSubjects
                FROM tutor_class tc
                GROUP BY tc.plan_id
            ) tc ON tc.plan_id = pl.id 
            WHERE
                (:#{#request.semesterId} IS NULL OR s.id LIKE CONCAT('%', :#{#request.semesterId}, '%'))
                AND (:#{#request.facilityCode} IS NULL OR f.code LIKE :#{#request.facilityCode})
                AND (:#{#request.departmentCode} IS NULL OR d.code LIKE :#{#request.departmentCode})
                AND (:status IS NULL OR pl.status LIKE :status)
            """, countQuery = """
            SELECT
                ROW_NUMBER() OVER(ORDER BY pl.created_date DESC) AS orderNumber,
                pl.id AS id,
                CONCAT(s.name, ' ', s.year) AS planName,
                b.name AS blockName,
                d.name AS departmentName,
                f.name AS facilityName,
                COALESCE(tc.numberSubjects, 0) AS numberSubjects,
                pl.plan_status AS status
            FROM
                plan pl
            LEFT JOIN block b ON
                b.id = pl.block_id
            LEFT JOIN semester s ON
                b.semester_id = s.id
            LEFT JOIN department_facility df ON
                df.id = pl.department_facility_id
            LEFT JOIN facility f ON
                df.id_facility = f.id
            LEFT JOIN department d ON
                d.id = df.id_department
            LEFT JOIN (
                SELECT tc.plan_id, COUNT(*) AS numberSubjects
                FROM tutor_class tc
                GROUP BY tc.plan_id
            ) tc ON tc.plan_id = pl.id 
            WHERE
                (:#{#request.semesterId} IS NULL OR s.id LIKE CONCAT('%', :#{#request.semesterId}, '%'))
                AND (:#{#request.facilityCode} IS NULL OR f.code LIKE :#{#request.facilityCode})
                AND (:#{#request.departmentCode} IS NULL OR d.code LIKE :#{#request.departmentCode})
                AND (:status IS NULL OR pl.status LIKE :status)
            """, nativeQuery = true)
    Page<PLPLPlanListResponse> getAllPlanning(Pageable pageable, PLPLPlanListRequest request, PlanStatus status);

}
