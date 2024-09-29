package udpm.hn.server.core.headdepartment.headplans.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headdepartment.headplans.model.request.HeadPlanRequest;
import udpm.hn.server.core.headdepartment.headplans.repository.HDHSHeadPlanExtendRepository;
import udpm.hn.server.core.headdepartment.headplans.service.HeadPlansService;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.infrastructure.constant.PlanStatus;
import udpm.hn.server.utils.Helper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HeadPlansServiceImpl implements HeadPlansService {

    private final HDHSHeadPlanExtendRepository planExtendRepository;

    @Override
    public ResponseObject<?> getPlans(HeadPlanRequest request) {
        log.info("Request: " + request.toString());
        PlanStatus planStatus = PlanStatus.fromString(request.getPlanStatus());
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(planExtendRepository.getAllPlans(pageable, request, planStatus)),
                HttpStatus.OK,
                "Lấy danh sách thành công."
        );
    }

    @Override
    public ResponseObject<?> approvePlan(String planId) {

        Optional<Plan> planOptional = planExtendRepository.findById(planId);

        if (planOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch không tồn tại");
        }

        //Chỉ phê duyệt những kế hoạch do planner đã phê duyệt
        if (!planOptional.get().getPlanStatus().equals(PlanStatus.PLANNER_APPROVED)) {
            return new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Cập nhật thất bại");
        }

        planOptional.map(plan -> {
            plan.setPlanStatus(PlanStatus.HEAD_DEPARTMENT_APPROVED);
            return planExtendRepository.save(plan);
        });

        return planOptional
                .map(plan -> new ResponseObject<>(null, HttpStatus.OK, "Cập nhật thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Cập nhật thất bại"));
    }
}
