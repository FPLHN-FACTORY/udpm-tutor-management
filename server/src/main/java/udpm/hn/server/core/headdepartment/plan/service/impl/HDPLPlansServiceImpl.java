package udpm.hn.server.core.headdepartment.plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import udpm.hn.server.core.planloghistory.model.request.PlanLogHistoryRequest;
import udpm.hn.server.core.planloghistory.service.PlanLogHistoryService;
import udpm.hn.server.entity.Block;
import udpm.hn.server.entity.Notification;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.infrastructure.config.websocket.model.NotifyModel;
import udpm.hn.server.infrastructure.config.websocket.service.NotificationService;
import udpm.hn.server.infrastructure.constant.BlockName;
import udpm.hn.server.infrastructure.constant.FunctionLogType;
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

    private final PlanLogHistoryService planLogHistoryService;

    private final HDPLNotificationRepository notificationRepository;

    private final NotificationService notificationService;

    @Value("${ws.topicPrefix}")
    private String topicPrefix;

    @Override
    public ResponseObject<?> getPlans(HDPLPlanListRequest request) {
        log.info("Request: {}", request.toString());
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(planRepository.getAllPlans(pageable, request)),
                HttpStatus.OK,
                "Lấy danh sách thành công."
        );
    }

    @Override
    public ResponseObject<?> approvePlan(String planId) {

        PlanLogHistoryRequest planLogHistory = new PlanLogHistoryRequest();
        planLogHistory.setTypeFunction(FunctionLogType.APPROVE_PLAN);
        planLogHistory.setAction("Phê duyệt kế hoạch");
        planLogHistory.setRoleStaff(Role.CHU_NHIEM_BO_MON.name());

        try {
            Optional<Plan> planOptional = planRepository.findById(planId);

            if (planOptional.isEmpty()) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch không tồn tại");
            }

            //Chỉ phê duyệt những kế hoạch do planner đã phê duyệt
            if (!planOptional.get().getPlanStatus().equals(PlanStatus.PLANNER_APPROVED)) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Cập nhật thất bại");
            }
            //Thêm 2 thông báo đến NGUOI_LAP_KE_HOACH và TRUONG_MON
            List<Notification> listNotificationSave = new ArrayList<>();
            Semester semesterNotification = planOptional.get().getBlock().getSemester();
            Block blockNotification = planOptional.get().getBlock();
            for (String role : List.of(Role.NGUOI_LAP_KE_HOACH.toString(), Role.TRUONG_MON.toString())) {
                Notification item = new Notification();
                item.setPlan(planOptional.get());
//                item.setContent("Kế hoạch của người lập kế hoạch(" + planOptional.get().getPlanner().getName() + ") đã được chủ nhiệm bộ môn phê duyệt.");
                item.setContent(
                        "Kế hoạch tại " + BlockName.toString(blockNotification.getName()) + " của kì " +
                                semesterNotification.getSemesterName() + " đã được chủ nhiệm bộ môn phê duyệt.");
                item.setStaff(planOptional.get().getPlanner());
                item.setSentTo(role);
                listNotificationSave.add(item);
            }

            notificationRepository.saveAll(listNotificationSave);

            notificationService.sendNotification(
                    topicPrefix,
                    NotifyModel.getRoles(
                            Role.NGUOI_LAP_KE_HOACH.name(),
                            Role.TRUONG_MON.name()
                    )
            );


            planOptional.map(plan -> {
                plan.setPlanStatus(PlanStatus.HEAD_DEPARTMENT_APPROVED);
                Plan planResult = planRepository.save(plan);
                if (planResult == null) {
                    planLogHistory.setStatus(false);
                    return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Có lỗi sảy ra khi duyệt kế hoạch");
                }
                planLogHistory.setPlanId(planResult.getId());
                planLogHistory.setStatus(true);
                return planResult;
            });

            return planOptional
                    .map(plan -> new ResponseObject<>(null, HttpStatus.OK, "Cập nhật thành công"))
                    .orElseGet(() -> new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Cập nhật thất bại"));
        } catch (Exception e) {
            planLogHistory.setStatus(false);
            e.printStackTrace();
        } finally {
            try {
                Boolean resultLog = planLogHistoryService.createPlanLogHistory(planLogHistory);
                if (!resultLog) {
                    System.err.println("Có lỗi xảy ra khi lưu log");
                }
            } catch (Exception e) {
                System.err.println("Lỗi khi ghi log: " + e.getMessage());
            }
        }
        return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Có lỗi sảy ra khi duyệt kế hoạch");
    }

    public ResponseObject<?> rejectPlan(HDPLRejectPlanRequest request) {
        PlanLogHistoryRequest planLogHistory = new PlanLogHistoryRequest();
        planLogHistory.setTypeFunction(FunctionLogType.REJECT_PLAN);
        planLogHistory.setAction("Từ chối kế hoạch");
        planLogHistory.setRoleStaff(Role.CHU_NHIEM_BO_MON.name());
        try {
            Optional<Plan> planOptional = planRepository.findById(request.getPlanId());
            if (planOptional.isEmpty()) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch không tồn tại");
            }
            if (!planOptional.get().getPlanStatus().equals(PlanStatus.PLANNER_APPROVED)) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Cập nhật thất bại");
            }

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

            planOptional.map(plan -> {
                plan.setPlanStatus(PlanStatus.PLANNING);
                plan.setReason(request.getReason());
                Plan planResult = planRepository.save(plan);
                if (planResult == null) {
                    planLogHistory.setStatus(false);
                    return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Có lỗi sảy ra khi duyệt kế hoạch");
                }
                planLogHistory.setRejectNote(request.getReason());
                planLogHistory.setPlanId(planResult.getId());
                planLogHistory.setStatus(true);
                return planResult;
            });
            return planOptional
                    .map(plan -> new ResponseObject<>(null, HttpStatus.OK, "Cập nhật thành công"))
                    .orElseGet(() -> new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Cập nhật thất bại"));
        } catch (Exception e) {
            planLogHistory.setStatus(false);
            e.printStackTrace();
        } finally {
            try {
                Boolean resultLog = planLogHistoryService.createPlanLogHistory(planLogHistory);
                if (!resultLog) {
                    System.err.println("Có lỗi xảy ra khi lưu log");
                }
            } catch (Exception e) {
                System.err.println("Lỗi khi ghi log: " + e.getMessage());
            }
        }
        return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Có lỗi sảy ra khi từ chối kế hoạch");
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
