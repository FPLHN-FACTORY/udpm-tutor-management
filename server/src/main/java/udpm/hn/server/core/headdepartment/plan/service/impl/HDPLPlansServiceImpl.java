package udpm.hn.server.core.headdepartment.plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headdepartment.plan.model.request.HDPLPlanInfoRequest;
import udpm.hn.server.core.headdepartment.plan.model.request.HDPLPlanListRequest;
import udpm.hn.server.core.headdepartment.plan.model.request.HDPLRejectPlanRequest;
import udpm.hn.server.core.headdepartment.plan.repository.HDPLNotificationRepository;
import udpm.hn.server.core.headdepartment.plan.repository.HDPLPlanExtendRepository;
import udpm.hn.server.core.headdepartment.plan.service.HDPLPlansService;
import udpm.hn.server.entity.Notification;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.infrastructure.constant.PlanStatus;
import udpm.hn.server.infrastructure.constant.Role;
import udpm.hn.server.utils.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HDPLPlansServiceImpl implements HDPLPlansService {

    private final HDPLPlanExtendRepository planRepository;

    private final HDPLNotificationRepository notificationRepository;

    @Override
    public ResponseObject<?> getPlans(HDPLPlanListRequest request) {
        log.info("Request: " + request.toString());
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(planRepository.getAllPlans(pageable, request)),
                HttpStatus.OK,
                "Lấy danh sách thành công."
        );
    }

    @Override
    public ResponseObject<?> approvePlan(String planId) {

        Optional<Plan> planOptional = planRepository.findById(planId);

        if (planOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch không tồn tại");
        }

        //Chỉ phê duyệt những kế hoạch do planner đã phê duyệt
        if (!planOptional.get().getPlanStatus().equals(PlanStatus.PLANNER_APPROVED)) {
            return new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Cập nhật thất bại");
        }

        planOptional.map(plan -> {
            plan.setPlanStatus(PlanStatus.HEAD_DEPARTMENT_APPROVED);
            return planRepository.save(plan);
        });

        //Thêm 2 thông báo đến NGUOI_LAP_KE_HOACH và TRUONG_MON
        List<Notification> listNotificationSave = new ArrayList<>();
        for (String role : List.of(Role.NGUOI_LAP_KE_HOACH.toString(), Role.TRUONG_MON.toString())) {
            Notification item = new Notification();
            item.setPlan(planOptional.get());
            item.setContent("Kế hoạch " + planOptional.get().getDescription() + " đã được chủ nhiệm bộ môn phê duyệt.");
            item.setStaff(planOptional.get().getPlanner());
            item.setSentTo(role);
            listNotificationSave.add(item);
        }

        notificationRepository.saveAll(listNotificationSave);

        return planOptional
                .map(plan -> new ResponseObject<>(null, HttpStatus.OK, "Cập nhật thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Cập nhật thất bại"));
    }

    public ResponseObject<?> rejectPlan(HDPLRejectPlanRequest request) {
        Optional<Plan> planOptional = planRepository.findById(request.getPlanId());
        if (planOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch không tồn tại");
        }
        if (!planOptional.get().getPlanStatus().equals(PlanStatus.PLANNER_APPROVED)) {
            return new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Cập nhật thất bại");
        }
        planOptional.map(plan -> {
            plan.setPlanStatus(PlanStatus.PLANNING);
            plan.setReason(request.getReason());
            return planRepository.save(plan);
        });

        //Thêm 2 thông báo đến NGUOI_LAP_KE_HOACH và TRUONG_MON
        List<Notification> listNotificationSave = new ArrayList<>();
        for (String role : List.of(Role.NGUOI_LAP_KE_HOACH.toString(), Role.TRUONG_MON.toString())) {
            Notification item = new Notification();
            item.setPlan(planOptional.get());
            item.setContent("Kế hoạch " + planOptional.get().getDescription() + " đã được chủ nhiệm bộ môn từ chối.");
            item.setStaff(planOptional.get().getPlanner());
            item.setSentTo(role);
            listNotificationSave.add(item);
        }
        notificationRepository.saveAll(listNotificationSave);

        return planOptional
                .map(plan -> new ResponseObject<>(null, HttpStatus.OK, "Cập nhật thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Cập nhật thất bại"));
    }

    @Override
    public ResponseObject<?> getPlansDetail(String planId) {
        return planRepository.getPlanById(planId)
                .map(plan -> new ResponseObject<>(plan, HttpStatus.OK, "Lấy thông tin môn học thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Môn học không tồn tại"));
    }

    @Override
    public ResponseObject<?> getPlansBySemester(HDPLPlanInfoRequest request) {
        return planRepository.getSemesterInfo(request)
                .map(plan -> new ResponseObject<>(plan, HttpStatus.OK, "Lấy thông tin học kỳ thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Học kỳ không tồn tại"));
    }

    @Override
    public ResponseObject<?> getPlansInfo(HDPLPlanInfoRequest request) {
        return new ResponseObject<>(
                planRepository.getPlanInfo(request),
                HttpStatus.OK,
                "Lấy danh sách kế hoach thành công!"
        );
    }

    @Override
    public ResponseObject<?> getPlansInfoById(String id) {
        return planRepository.getPlanInfoById(id)
                .map(plan -> new ResponseObject<>(plan, HttpStatus.OK, "Lấy thông tin kế hoạch thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Kế hoạch không tồn tại"));
    }

}
