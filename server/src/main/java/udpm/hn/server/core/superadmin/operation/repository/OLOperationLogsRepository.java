package udpm.hn.server.core.superadmin.operation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.superadmin.operation.model.request.OperationLogsFilterRequest;
import udpm.hn.server.core.superadmin.activity.model.response.OperationLogsResponse;
import udpm.hn.server.repository.OperationLogsRepository;

@Repository
public interface OLOperationLogsRepository extends OperationLogsRepository {

    @Query(
            value = """
        SELECT
            ROW_NUMBER() OVER (ORDER BY l.created_date DESC) AS orderNumber,
            l.id AS id,
            l.workstation,
            l.user_name AS userName,
            l.email as email,
            l.code as code,
            l.api as api,
            l.type_function AS typeFunction,
            l.created_date AS createdDate,
            l.status as status
        FROM
            operation_log l
        WHERE
            (:#{#req.emailOrNameOrCode} IS NULL 
                OR l.email LIKE %:#{#req.emailOrNameOrCode}% 
                OR l.user_name LIKE %:#{#req.emailOrNameOrCode}% 
                OR l.code LIKE %:#{#req.emailOrNameOrCode}%) 
            AND (:#{#req.typeFunction} IS NULL OR l.type_function = :#{#req.typeFunction})
            AND (:#{#req.fromDate} IS NULL OR l.created_date >= :#{#req.fromDate})
            AND (:#{#req.toDate} IS NULL OR l.created_date <= :#{#req.toDate})
            AND (:#{#req.status} IS NULL OR l.status = :#{#req.status})
        ORDER BY l.created_date DESC
    """,
            countQuery = """
        SELECT
            COUNT(l.id)
        FROM
            operation_log l
        WHERE
            (:#{#req.emailOrNameOrCode} IS NULL 
                OR l.email LIKE %:#{#req.emailOrNameOrCode}% 
                OR l.user_name LIKE %:#{#req.emailOrNameOrCode}% 
                OR l.code LIKE %:#{#req.emailOrNameOrCode}%) 
            AND (:#{#req.typeFunction} IS NULL OR l.type_function = :#{#req.typeFunction})
            AND (:#{#req.fromDate} IS NULL OR l.created_date >= :#{#req.fromDate})
            AND (:#{#req.toDate} IS NULL OR l.created_date <= :#{#req.toDate})
            AND (:#{#req.status} IS NULL OR l.status = :#{#req.status})
            ORDER BY l.created_date DESC
    """,
            nativeQuery = true
    )
    Page<OperationLogsResponse> getAllLogSystems(@Param("req") OperationLogsFilterRequest req, Pageable pageable);
    @Query(value = """
            SELECT
            	 l.id AS id,
                        l.workstation,
                        l.user_name AS userName,
                        l.email as email,
                        l.code as code,
                        l.api as api,
                        l.request as request,
                        l.response as response,
                        l.type_function AS typeFunction,
                        l.created_date AS createdDate
            FROM
            	operation_log l
            WHERE
                l.id = :id
            """, countQuery = """
            SELECT
            	COUNT(d.id)
            FROM
            	operation_log l
            WHERE
                l.id = :id
            """, nativeQuery = true)
    OperationLogsResponse getDetailOperationLog(String id);

}
