package udpm.hn.server.core.authentication.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.repository.StaffRepository;

import java.util.Optional;

@Repository
public interface AStaffExtendRepository extends StaffRepository {

    Optional<Staff> findByEmailFe(String accountFE);

    Optional<Staff> findByEmailFpt(String accountFPT);

}
