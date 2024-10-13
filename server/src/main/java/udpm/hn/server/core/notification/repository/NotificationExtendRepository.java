package udpm.hn.server.core.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.notification.model.request.NotificationRequest;
import udpm.hn.server.core.notification.model.response.NotificationResponse;
import udpm.hn.server.repository.NotificationRepository;

@Repository
public interface NotificationExtendRepository extends NotificationRepository {

    @Query(
            value = """
                            SELECT
                                n.id AS id,
                                n.content AS content,
                                IF(user_received LIKE CONCAT('%',:#{#request.userId},'%'), 'true', 'false') AS status,
                                n.created_date AS createdDate
                            FROM notification n LEFT JOIN plan p on n.plan_id = p.id
                                                LEFT JOIN department_facility df on p.department_facility_id = df.id
                                                LEFT JOIN department d on df.id_department = d.id
                                                LEFT JOIN facility f on df.id_facility = f.id
                            WHERE n.send_to LIKE CONCAT('%',:#{#request.keyWord},'%')
                                AND d.code = :#{#request.departmentCode}
                                AND f.code = :#{#request.facilityCode}
                            ORDER BY n.created_date DESC
                    """,
            nativeQuery = true
    )
    Page<NotificationResponse> getAllNotifications(Pageable pageable, NotificationRequest request);

    @Query(
            value =
                    """
                            SELECT
                                COUNT(*) AS count
                            FROM notification n LEFT JOIN plan p on n.plan_id = p.id
                                                LEFT JOIN department_facility df on p.department_facility_id = df.id
                                                LEFT JOIN department d on df.id_department = d.id
                                                LEFT JOIN facility f on df.id_facility = f.id
                            WHERE n.send_to LIKE CONCAT('%',:#{#request.keyWord},'%')
                                AND d.code = :#{#request.departmentCode}
                                AND f.code = :#{#request.facilityCode}
                                AND IF(user_received LIKE CONCAT('%',:#{#request.userId},'%'), 'true', 'false') = 'false'
                            """,
            nativeQuery = true
    )
    Integer countNotification(NotificationRequest request);


}
