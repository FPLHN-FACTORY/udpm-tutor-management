package udpm.hn.server.core.headdepartment.planner.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.headdepartment.planner.model.request.HDPLNAssignedPlannerRequest;
import udpm.hn.server.core.headdepartment.planner.model.request.HDPLNPlannerListRequest;
import udpm.hn.server.core.headdepartment.planner.model.response.HDPLNPlannerResponse;
import udpm.hn.server.repository.StaffRepository;

@Repository
public interface HDPLNStaffsRepository extends StaffRepository {

    @Query(
            value = """
                    SELECT 
                        ROW_NUMBER() OVER (ORDER BY s.id DESC ) as orderNumber,
                        s.id as id,
                        s.staff_code as staffCode,
                        s.name as staffName,
                        s.email_fpt as emailFPT,
                        s.email_fe as emailFE,
                        IF(COUNT(CASE WHEN r.code = 'NGUOI_LAP_KE_HOACH' THEN 1 END) > 0, TRUE, FALSE) as isAssigned
                    FROM
                        staff s
                    LEFT JOIN staff_major_facility smf ON s.id = smf.id_staff
                    LEFT JOIN major_facility mf ON mf.id = smf.id_major_facility
                    LEFT JOIN department_facility df ON mf.id_department_facility = df.id
                    LEFT JOIN facility f ON f.id = df.id_facility
                    LEFT JOIN department d ON d.id = df.id_department
                    LEFT JOIN head_subject_by_semester hsbs ON hsbs.id_staff = s.id AND hsbs.id_semester = :#{#request.currentSemesterId}
                    LEFT JOIN semester se ON se.id = hsbs.id_semester
                    LEFT JOIN block b ON b.id = :#{#request.currentBlockId}
                    LEFT JOIN staff_role sr ON sr.id_staff = s.id
                    LEFT JOIN role r ON r.id = sr.id_role
                    WHERE
                         s.id != :#{#request.currentUserId}
                        AND d.code = :#{#request.currentDepartmentCode}
                        AND f.code = :#{#request.currentFacilityCode}
                        AND ((:#{#request.q} IS NULL OR s.staff_code LIKE CONCAT('%',:#{#request.q},'%')))
                        AND ((:#{#request.namePlanner} IS NULL OR s.name LIKE CONCAT('%',:#{#request.namePlanner},'%')))
                    GROUP BY s.id, s.staff_code, s.name, s.email_fpt, s.email_fe
                    ORDER BY s.created_date DESC
                    """,
            countQuery = """
                    SELECT COUNT(DISTINCT s.id)
                    FROM
                        staff s
                    LEFT JOIN staff_major_facility smf ON s.id = smf.id_staff
                    LEFT JOIN major_facility mf ON mf.id = smf.id_major_facility
                    LEFT JOIN department_facility df ON mf.id_department_facility = df.id
                    LEFT JOIN facility f ON f.id = df.id_facility
                    LEFT JOIN department d ON d.id = df.id_department
                    LEFT JOIN head_subject_by_semester hsbs ON hsbs.id_staff = s.id AND hsbs.id_semester = :#{#request.currentSemesterId}
                    LEFT JOIN semester se ON se.id = hsbs.id_semester
                    LEFT JOIN block b ON b.id = :#{#request.currentBlockId}
                    LEFT JOIN staff_role sr ON sr.id_staff = s.id
                    LEFT JOIN role r ON r.id = sr.id_role
                    WHERE
                         s.id != :#{#request.currentUserId}
                        AND d.code = :#{#request.currentDepartmentCode}
                        AND f.code = :#{#request.currentFacilityCode}
                        AND ((:#{#request.q} IS NULL OR s.staff_code LIKE CONCAT('%',:#{#request.q},'%')))
                        AND ((:#{#request.namePlanner} IS NULL OR s.name LIKE CONCAT('%',:#{#request.namePlanner},'%')))
                        ORDER BY s.created_date DESC
                    """,
            nativeQuery = true
    )
    Page<HDPLNPlannerResponse> getAllPlannerManager(Pageable pageable, HDPLNPlannerListRequest request);

    @Query(
            value = """
                    SELECT s.id
                    FROM staff s
                    LEFT JOIN staff_major_facility smf ON s.id = smf.id_staff
                    LEFT JOIN major_facility mf ON mf.id = smf.id_major_facility
                    LEFT JOIN department_facility df ON mf.id_department_facility = df.id
                    LEFT JOIN facility f ON f.id = df.id_facility
                    LEFT JOIN department d ON d.id = df.id_department
                    LEFT JOIN staff_role sr ON sr.id_staff = s.id
                    LEFT JOIN role r ON r.id = sr.id_role
                    WHERE 
                        s.id != :#{#request.currentUserId}
                        AND d.code = :#{#request.currentDepartmentCode}
                        AND f.code = :#{#request.currentFacilityCode}
                        AND r.code = 'NGUOI_LAP_KE_HOACH'
                    LIMIT 1
                    """,
            nativeQuery = true
    )
    String findSubjectPlannerIdByRole(HDPLNAssignedPlannerRequest request);

}
