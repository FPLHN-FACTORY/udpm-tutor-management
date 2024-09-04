package udpm.hn.server.infrastructure.security.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.repository.StaffRepository;

import java.util.Optional;

@Repository
public interface StaffAuthRepository extends StaffRepository {

    Optional<Staff> findByEmailFe(String accountFE);

    Optional<Staff> findByEmailFpt(String accountFPT);

}
