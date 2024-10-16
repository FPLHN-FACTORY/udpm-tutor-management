package udpm.hn.server.infrastructure.config.job.staff.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.StaffRepository;

import java.util.Optional;

@Repository
public interface ConfigStaffCustomRepository extends StaffRepository {

    Optional<Staff> findByStaffCodeAndStatus(String code, EntityStatus status);

    Optional<Staff> findByEmailFe(String emailFe);

    Optional<Staff> findByEmailFpt(String emailFpt);

}
