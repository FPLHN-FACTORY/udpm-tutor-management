package udpm.hn.server.core.headsubject.plan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLPlanInfoRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLPlanListRequest;
import udpm.hn.server.core.headsubject.plan.model.response.HSPLPlanDetailResponse;
import udpm.hn.server.core.headsubject.plan.model.response.HSPLPlanInfoResponse;
import udpm.hn.server.core.headsubject.plan.model.response.HSPLPlanListResponse;
import udpm.hn.server.core.headsubject.plan.model.response.HSPLSemesterInfoResponse;
import udpm.hn.server.entity.Block;
import udpm.hn.server.entity.DepartmentFacility;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.repository.PlanRepository;
import udpm.hn.server.repository.StaffRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HSPLStaffsRepository extends StaffRepository {
}
