package udpm.hn.server.core.admin.staff.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.staff.model.request.HOStaffRequest;
import udpm.hn.server.core.admin.staff.model.response.HOStaffDetailResponse;
import udpm.hn.server.core.admin.staff.model.response.HOStaffResonpse;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.StaffRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HOStaffRepository extends StaffRepository {

    @Query(
            value = """
                    SELECT  s.id AS id,
                            s.name AS name,
                            s.staff_code AS staffCode,
                            s.email_fe AS emailFE,
                            s.email_fpt AS emailFpt
                    FROM staff s
                    WHERE s.status = 0
                    AND (:#{#req.searchQuery} IS NULL 
                             OR :#{#req.searchQuery} LIKE ''
                             OR s.name LIKE CONCAT('%',:#{#req.searchQuery},'%')
                             OR s.staff_code LIKE CONCAT('%',:#{#req.searchQuery},'%')
                             OR s.email_fe LIKE CONCAT('%',:#{#req.searchQuery},'%')
                             OR s.email_fpt LIKE CONCAT('%',:#{#req.searchQuery},'%'))
                    """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM staff s
                    WHERE s.status = 0
                    AND (:#{#req.searchQuery} IS NULL 
                             OR :#{#req.searchQuery} LIKE ''
                             OR s.name LIKE CONCAT('%',:#{#req.searchQuery},'%')
                             OR s.staff_code LIKE CONCAT('%',:#{#req.searchQuery},'%')
                             OR s.email_fe LIKE CONCAT('%',:#{#req.searchQuery},'%')
                             OR s.email_fpt LIKE CONCAT('%',:#{#req.searchQuery},'%'))
                    """,
            nativeQuery = true)
    Page<HOStaffResonpse> getStaffs(Pageable pageable, HOStaffRequest req);

    @Query(value = """
            SELECT  s.id AS id,
                    s.name AS name,
                    s.staff_code AS staffCode,
                    s.email_fe AS emailFE,
                    s.email_fpt AS emailFpt
            FROM staff s
            WHERE s.id LIKE :id
            """, nativeQuery = true)
    Optional<HOStaffDetailResponse> getStaff(String id);

    List<Staff> findByStaffCode(String staffCode);

    Optional<Staff> findByIdAndStatus(String id, EntityStatus status);

}
