package udpm.hn.server.core.planloghistory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.planloghistory.model.request.PlanLogHistoryFilterRequest;
import udpm.hn.server.core.planloghistory.model.request.PlanLogHistoryRequest;
import udpm.hn.server.core.planloghistory.model.response.PlanLogHistoryResponse;
import udpm.hn.server.repository.PlanLogHistoryRepository;

@Repository
public interface PLSPlanLogHistoryRepository extends PlanLogHistoryRepository {

//    @Query(
//            value = """
//                SELECT DISTINCT
//                    ROW_NUMBER() OVER (ORDER BY plh.created_date DESC) AS orderNumber,
//                    plh.id AS id,
//                    plh.created_date AS createDate,
//                    plh.status AS status,
//                    plh.action AS actionName,
//                    plh.code_staff AS codeStaff,
//                    plh.email AS email,
//                    plh.role_staff AS roleStaff,
//                    plh.timestamp AS timeStampDate,
//                    plh.type_function AS typeFunction,
//                    CONCAT(plh.user_name, ' - ',  plh.code_staff) AS userName,
//                    b.name AS nameBlock,
//                    CONCAT(ss.name, ' - ',  ss.year) AS namePlan
//                FROM
//                    plan_log_history plh
//                LEFT JOIN
//                    plan p ON p.id = plh.id_plan
//                LEFT JOIN
//                    block b ON b.id = p.block_id
//                LEFT JOIN
//                    semester ss on ss.id = b.semester_id
//                LEFT JOIN
//                    staff s ON p.planner_id = s.id
//                LEFT JOIN
//                    staff_role sr ON sr.id_staff = s.id
//                LEFT JOIN
//                    role rl ON sr.id_role = rl.id
//                LEFT JOIN
//                    facility f ON rl.id_facility = f.id
//                WHERE
//                    (:#{#request.staffId} IS NULL OR s.id = :#{#request.staffId})
//                    AND (:#{#request.blockId} IS NULL OR b.id = :#{#request.blockId})
//                    AND (:#{#request.semesterId} IS NULL OR ss.id = :#{#request.semesterId})
//                    AND (:#{#request.roleStaff} IS NULL OR plh.role_staff IN (:#{#request.roleStaffList}))
//                    AND (:#{#request.facilityId} IS NULL OR f.id = :#{#request.facilityId})
//                    AND (:#{#request.logCodeRole} IS NULL OR rl.code = :#{#request.logCodeRole})
//                    AND (:#{#request.status} IS NULL OR plh.status = :#{#request.status})
//                    AND (:#{#request.fromDate} IS NULL OR plh.timestamp >= :#{#request.fromDate})
//                    AND (:#{#request.toDate} IS NULL OR plh.timestamp <= :#{#request.toDate})
//                    AND (:#{#request.logType} IS NULL OR plh.type_function = :#{#request.logType})
//                    ORDER BY plh.created_date DESC
//                """,
//            countQuery = """
//                SELECT COUNT(DISTINCT plh.id)
//                FROM
//                    plan_log_history plh
//                LEFT JOIN
//                    plan p ON p.id = plh.id_plan
//                LEFT JOIN
//                    block b ON b.id = p.block_id
//                LEFT JOIN
//                    semester ss on ss.id = b.semester_id
//                LEFT JOIN
//                    staff s ON p.planner_id = s.id
//                LEFT JOIN
//                    staff_role sr ON sr.id_staff = s.id
//                LEFT JOIN
//                    role rl ON sr.id_role = rl.id
//                LEFT JOIN
//                    facility f ON rl.id_facility = f.id
//                WHERE
//                    (:#{#request.staffId} IS NULL OR s.id = :#{#request.staffId})
//                    AND (:#{#request.semesterId} IS NULL OR ss.id = :#{#request.semesterId})
//                    AND (:#{#request.blockId} IS NULL OR b.id = :#{#request.blockId})
//                    AND (:#{#request.roleStaff} IS NULL OR plh.role_staff IN (:#{#request.roleStaffList}))
//                    AND (:#{#request.facilityId} IS NULL OR f.id = :#{#request.facilityId})
//                    AND (:#{#request.logCodeRole} IS NULL OR rl.code = :#{#request.logCodeRole})
//                    AND (:#{#request.status} IS NULL OR plh.status = :#{#request.status})
//                    AND (:#{#request.fromDate} IS NULL OR plh.timestamp >= :#{#request.fromDate})
//                    AND (:#{#request.toDate} IS NULL OR plh.timestamp <= :#{#request.toDate})
//                    AND (:#{#request.logType} IS NULL OR plh.type_function = :#{#request.logType})
//                    ORDER BY plh.created_date DESC
//                """,
//            nativeQuery = true
//    )
//    Page<PlanLogHistoryResponse> getAllPlanLogHistory(
//            @Param("request") PlanLogHistoryFilterRequest request,
//            Pageable pageable);

    @Query(
            value = """
                        SELECT DISTINCT 
                            ROW_NUMBER() OVER (ORDER BY plh.created_date DESC) AS orderNumber,
                            plh.id AS id,
                            plh.created_date AS createDate,
                            plh.status AS status,
                            plh.action AS actionName,
                            plh.code_staff AS codeStaff,
                            plh.email AS email,
                            plh.role_staff AS roleStaff,
                            plh.timestamp AS timeStampDate,
                            plh.type_function AS typeFunction,
                            CONCAT(plh.user_name, ' - ', plh.code_staff) AS userName,
                            b.name AS nameBlock,
                            CONCAT(ss.name, ' - ', ss.year) AS namePlan
                        FROM 
                            plan_log_history plh
                        LEFT JOIN 
                            plan p ON p.id = plh.id_plan
                        LEFT JOIN 
                            block b ON b.id = p.block_id
                        LEFT JOIN 
                            semester ss ON ss.id = b.semester_id
                        LEFT JOIN 
                            staff s ON p.planner_id = s.id
                        LEFT JOIN 
                            staff_role sr ON sr.id_staff = s.id
                        LEFT JOIN 
                            role rl ON sr.id_role = rl.id
                        LEFT JOIN 
                            facility f ON rl.id_facility = f.id
                        WHERE 
                            (:#{#request.staffId} IS NULL OR s.id = :#{#request.staffId} OR plh.id_plan IS NULL) 
                            AND (:#{#request.blockId} IS NULL OR b.id = :#{#request.blockId} OR plh.id_plan IS NULL) 
                            AND (:#{#request.semesterId} IS NULL OR ss.id = :#{#request.semesterId} OR plh.id_plan IS NULL) 
                            AND (:#{#request.roleStaff} IS NULL OR plh.role_staff IN (:#{#request.roleStaffList})) 
                            AND (:#{#request.facilityId} IS NULL OR f.id = :#{#request.facilityId} OR plh.id_plan IS NULL) 
                            AND (:#{#request.logCodeRole} IS NULL OR rl.code = :#{#request.logCodeRole} OR plh.id_plan IS NULL) 
                            AND (:#{#request.status} IS NULL OR plh.status = :#{#request.status}) 
                            AND (:#{#request.fromDate} IS NULL OR plh.timestamp >= :#{#request.fromDate}) 
                            AND (:#{#request.toDate} IS NULL OR plh.timestamp <= :#{#request.toDate}) 
                            AND (:#{#request.logType} IS NULL OR plh.type_function = :#{#request.logType})
                        ORDER BY 
                            plh.created_date DESC
                    """,
            countQuery = """
                        SELECT COUNT(DISTINCT plh.id)
                        FROM 
                            plan_log_history plh
                        LEFT JOIN 
                            plan p ON p.id = plh.id_plan
                        LEFT JOIN 
                            block b ON b.id = p.block_id
                        LEFT JOIN 
                            semester ss ON ss.id = b.semester_id
                        LEFT JOIN 
                            staff s ON p.planner_id = s.id
                        LEFT JOIN 
                            staff_role sr ON sr.id_staff = s.id
                        LEFT JOIN 
                            role rl ON sr.id_role = rl.id
                        LEFT JOIN 
                            facility f ON rl.id_facility = f.id
                        WHERE 
                            (:#{#request.staffId} IS NULL OR s.id = :#{#request.staffId} OR plh.id_plan IS NULL) 
                            AND (:#{#request.blockId} IS NULL OR b.id = :#{#request.blockId} OR plh.id_plan IS NULL) 
                            AND (:#{#request.semesterId} IS NULL OR ss.id = :#{#request.semesterId} OR plh.id_plan IS NULL) 
                            AND (:#{#request.roleStaff} IS NULL OR plh.role_staff IN (:#{#request.roleStaffList})) 
                            AND (:#{#request.facilityId} IS NULL OR f.id = :#{#request.facilityId} OR plh.id_plan IS NULL) 
                            AND (:#{#request.logCodeRole} IS NULL OR rl.code = :#{#request.logCodeRole} OR plh.id_plan IS NULL) 
                            AND (:#{#request.status} IS NULL OR plh.status = :#{#request.status}) 
                            AND (:#{#request.fromDate} IS NULL OR plh.timestamp >= :#{#request.fromDate}) 
                            AND (:#{#request.toDate} IS NULL OR plh.timestamp <= :#{#request.toDate}) 
                            AND (:#{#request.logType} IS NULL OR plh.type_function = :#{#request.logType})
                        ORDER BY 
                            plh.created_date DESC
                    """,
            nativeQuery = true
    )
    Page<PlanLogHistoryResponse> getAllPlanLogHistory(
            @Param("request") PlanLogHistoryFilterRequest request,
            Pageable pageable);


    @Query(
            value = """
                        SELECT 
                            plh.id AS id,
                            plh.created_date AS createDate,
                            plh.status AS status,
                            plh.action AS actionName,
                            plh.code_staff AS codeStaff,
                            plh.email AS email,
                            plh.role_staff AS roleStaff,
                            plh.timestamp AS timeStampDate,
                            plh.type_function AS typeFunction,
                            plh.end_date AS endDate,
                            plh.start_date AS startDate,
                            plh.description AS description,
                            plh.number_of_class AS numberOfClass,
                            plh.number_of_lecture AS numberOfLecture,
                            plh.staff_info AS staffInfo,
                            plh.code_tutor_class_detail AS codeTutorClassDetail,
                            plh.formality AS formality,
                            plh.reject_note AS rejectNote,
                            plh.student_tutor AS studentTutor,
                            plh.room_plan AS roomPlan,
                            CONCAT(plh.user_name, ' - ',  plh.code_staff) AS userName,
                            b.name AS nameBlock,
                            CONCAT(ss.name, ' - ',  ss.year) AS namePlan
                        FROM 
                            plan_log_history plh
                        LEFT JOIN 
                            plan p ON p.id = plh.id_plan
                        LEFT JOIN 
                            block b ON b.id = p.block_id
                        LEFT JOIN
                            semester ss ON ss.id = b.semester_id
                        WHERE 
                            plh.id = :id
                    """,
            nativeQuery = true
    )
    PlanLogHistoryResponse getPlanLogHistoryById(@Param("id") String id);


}
