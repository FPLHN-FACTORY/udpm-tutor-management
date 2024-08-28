package udpm.hn.server.infrastructure.config.job.staff.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.repository.StaffRepository;

import java.util.List;

@Repository
public interface StaffCustomRepository extends StaffRepository {

    List<Staff> findByStaffCode(String id);

}
