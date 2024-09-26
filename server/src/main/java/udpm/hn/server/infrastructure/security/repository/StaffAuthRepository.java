package udpm.hn.server.infrastructure.security.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.planner.plan.model.request.PLPLPlanListRequest;
import udpm.hn.server.core.planner.plan.model.response.PLPLPlanListResponse;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.repository.StaffRepository;

import java.util.Optional;

@Repository
public interface StaffAuthRepository extends StaffRepository {

    Optional<Staff> findByEmailFe(String accountFE);

    Optional<Staff> findByEmailFpt(String accountFPT);


}
