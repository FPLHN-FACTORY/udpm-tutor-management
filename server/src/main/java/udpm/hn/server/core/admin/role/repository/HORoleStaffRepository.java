package udpm.hn.server.core.admin.role.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.StaffRole;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.StaffRoleRepository;

import java.util.List;

@Repository
public interface HORoleStaffRepository extends StaffRoleRepository {

    List<StaffRole> findAllByRole_IdAndStatus(String roleId, EntityStatus status);

}
