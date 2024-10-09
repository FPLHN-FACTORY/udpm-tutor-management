package udpm.hn.server.core.operationlogs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.operationlogs.model.request.UserActivityLogFilterRequest;
import udpm.hn.server.core.operationlogs.model.response.UserActivityLogResponse;
import udpm.hn.server.repository.UserActivityLogRepository;

@Repository
public interface OLUserActivityLogRepository extends UserActivityLogRepository {

    @Query(
            value = """
        SELECT
            ROW_NUMBER() OVER (ORDER BY l.login_time DESC) AS orderNumber,
            l.id AS id,
            l.workstation AS workstation,
            l.email AS email,
            l.name AS name,
            l.code AS code,
            l.session_id AS sessionId,
            l.operation AS operation,
            l.login_time AS loginTime,
            l.logout_time AS logoutTime,
            l.status AS status
        FROM
            user_activity_log l
        WHERE
            (:#{#req.email} IS NULL OR l.email LIKE %:#{#req.email}%)
            AND (:#{#req.nameOrCode} IS NULL 
                OR l.name LIKE %:#{#req.nameOrCode}% 
                OR l.code LIKE %:#{#req.nameOrCode}%)
            AND (:#{#req.fromDate} IS NULL OR l.created_date >= :#{#req.fromDate})
            AND (:#{#req.toDate} IS NULL OR l.created_date <= :#{#req.toDate})
            AND (:#{#req.status} IS NULL OR l.status = :#{#req.status})
            AND (:#{#req.statusUserActivity} IS NULL OR l.status_user_activity = :#{#req.statusUserActivity})
        ORDER BY l.login_time DESC
    """,
            countQuery = """
        SELECT
            COUNT(l.id)
        FROM
            user_activity_log l
        WHERE
            (:#{#req.email} IS NULL OR l.email LIKE %:#{#req.email}%)
            AND (:#{#req.nameOrCode} IS NULL 
                OR l.name LIKE %:#{#req.nameOrCode}% 
                OR l.code LIKE %:#{#req.nameOrCode}%)
            AND (:#{#req.fromDate} IS NULL OR l.created_date >= :#{#req.fromDate})
            AND (:#{#req.toDate} IS NULL OR l.created_date <= :#{#req.toDate})
            AND (:#{#req.status} IS NULL OR l.status = :#{#req.status})
            AND (:#{#req.statusUserActivity} IS NULL OR l.status_user_activity = :#{#req.statusUserActivity})
            ORDER BY l.created_date DESC
    """,
            nativeQuery = true
    )
    Page<UserActivityLogResponse> getAllUserActivityLogs(@Param("req") UserActivityLogFilterRequest req, Pageable pageable);

}
