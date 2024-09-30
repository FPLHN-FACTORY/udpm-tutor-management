package udpm.hn.server.core.headdepartment.plan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.headdepartment.plan.model.request.HDPLPlanInfoRequest;
import udpm.hn.server.core.headdepartment.plan.model.request.HDPLPlanListRequest;
import udpm.hn.server.core.headdepartment.plan.model.response.HDPLPlanDetailResponse;
import udpm.hn.server.core.headdepartment.plan.model.response.HDPLPlanInfoResponse;
import udpm.hn.server.core.headdepartment.plan.model.response.HDPLPlanListResponse;
import udpm.hn.server.core.headdepartment.plan.model.response.HDPLSemesterInfoResponse;
import udpm.hn.server.repository.PlanRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HDPLPlanExtendRepository extends PlanRepository {

    @Query(value = """
            SELECT
                ROW_NUMBER() OVER(ORDER BY b.start_time DESC) AS orderNumber,
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
                AND (:#{#request.planStatus} IS NULL OR pl.plan_status LIKE :#{#request.planStatus})
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
                AND (:#{#request.planStatus} IS NULL OR pl.plan_status LIKE :#{#request.planStatus})
            """, nativeQuery = true)
    Page<HDPLPlanListResponse> getAllPlans(Pageable pageable, HDPLPlanListRequest request);

    @Query(value = """
            SELECT
                ROW_NUMBER() OVER(ORDER BY pl.created_date DESC) AS orderNumber,
                pl.id AS id,
                CONCAT(s.name, ' ', s.year) AS planName,
                b.name AS blockName,
                b.id AS blockId,
                s.name AS semesterName,
                s.id AS semesterId,
                pl.description AS description,
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
                pl.id = :planId
            """, nativeQuery = true)
    Optional<HDPLPlanDetailResponse> getPlanById(String planId);

    @Query(value = """
            SELECT
                CONCAT('Học kỳ' ,' ',s.name, ' ', s.year) AS planName,
                s.start_time AS startTime,
                s.end_time AS endTime,
                d.name AS departmentName,
                f.name AS facilityName
            FROM
                semester s
            LEFT JOIN facility f ON
                f.code = :#{#request.facilityCode}
            LEFT JOIN department d ON
                d.code = :#{#request.departmentCode}
                where s.id = :#{#request.semesterId}
            """, nativeQuery = true)
    Optional<HDPLSemesterInfoResponse> getSemesterInfo(HDPLPlanInfoRequest request);

    @Query(value = """
            SELECT
                b.name as blockName,
                b.start_time AS startTime,
                b.end_time AS endTime,
                pl.plan_status AS status,
                f.name AS facilityName,
                COALESCE(tc.numberSubjects, 0) AS numberSubjects,
                COALESCE(tc.numberClasses, 0) AS numberClasses
            FROM
                plan pl
            LEFT JOIN (
                SELECT tc.plan_id, COUNT(*) AS numberSubjects, SUM(tc.number_of_classes) AS numberClasses
                FROM tutor_class tc
                GROUP BY tc.plan_id
            ) tc ON tc.plan_id = pl.id
            LEFT JOIN block b ON
                pl.block_id = b.id
            LEFT JOIN semester s ON
                s.id = b.semester_id
            LEFT JOIN department_facility df ON
                df.id = pl.department_facility_id        
            LEFT JOIN facility f ON
                f.id = df.id_facility
            LEFT JOIN department d ON
                d.id = df.id_department
            where s.id = :#{#request.semesterId}
            AND (:#{#request.facilityCode} IS NULL OR f.code LIKE :#{#request.facilityCode})
            AND (:#{#request.departmentCode} IS NULL OR d.code LIKE :#{#request.departmentCode})
            ORDER BY b.start_time
            """, nativeQuery = true)
    List<HDPLPlanInfoResponse> getPlanInfo(HDPLPlanInfoRequest request);

    @Query(value = """
            SELECT
            pl.id AS id,
            CONCAT(s.name, ' ', s.year) AS planName,
            b.name as blockName,
            pl.plan_status AS status,
            f.name AS facilityName,
            b.start_time AS startTime,
            b.end_time AS endTime,
            COALESCE(tc.numberSubjects, 0) AS numberSubjects,
            COALESCE(tc.numberClasses, 0) AS numberClasses
            FROM
                plan pl
            LEFT JOIN (
                SELECT tc.plan_id, COUNT(*) AS numberSubjects, SUM(tc.number_of_classes) AS numberClasses
                FROM tutor_class tc
                GROUP BY tc.plan_id
            ) tc ON tc.plan_id = pl.id
            LEFT JOIN block b ON
                pl.block_id = b.id
            LEFT JOIN semester s ON
                s.id = b.semester_id
            LEFT JOIN department_facility df ON
                df.id = pl.department_facility_id        
            LEFT JOIN facility f ON
                f.id = df.id_facility
            LEFT JOIN department d ON
                d.id = df.id_department
            where pl.id = :planId
            """, nativeQuery = true)
    Optional<HDPLPlanInfoResponse> getPlanInfoById(String planId);

}
