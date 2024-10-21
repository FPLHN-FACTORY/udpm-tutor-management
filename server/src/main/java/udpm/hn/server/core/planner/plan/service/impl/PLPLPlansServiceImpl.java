package udpm.hn.server.core.planner.plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.planloghistory.model.request.PlanLogHistoryRequest;
import udpm.hn.server.core.planloghistory.service.PlanLogHistoryService;
import udpm.hn.server.core.planner.plan.model.request.PLPLCreatePlanRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLPlanInfoRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLPlanListRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLUpdatePlanRequest;
import udpm.hn.server.core.planner.plan.repository.PLPLBlocksRepository;
import udpm.hn.server.core.planner.plan.repository.PLPLDepartmentFacilitysRepository;
import udpm.hn.server.core.planner.plan.repository.PLPLPlansRepository;
import udpm.hn.server.core.planner.plan.repository.PLPLStaffsRepository;
import udpm.hn.server.core.planner.plan.repository.PlanNotificationRepository;
import udpm.hn.server.core.planner.plan.service.PLPLPlansService;
import udpm.hn.server.entity.Block;
import udpm.hn.server.entity.Department;
import udpm.hn.server.entity.DepartmentFacility;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.entity.Notification;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.config.email.service.EmailService;
import udpm.hn.server.infrastructure.config.googleform.service.GoogleFormService;
import udpm.hn.server.infrastructure.config.websocket.model.NotifyModel;
import udpm.hn.server.infrastructure.config.websocket.service.NotificationService;
import udpm.hn.server.infrastructure.constant.BlockName;
import udpm.hn.server.infrastructure.constant.FunctionLogType;
import udpm.hn.server.infrastructure.constant.PlanStatus;
import udpm.hn.server.infrastructure.constant.Role;
import udpm.hn.server.utils.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PLPLPlansServiceImpl implements PLPLPlansService {

    private final PLPLPlansRepository plplPlansRepository;

    private final PLPLStaffsRepository plplStaffsRepository;

    private final PLPLDepartmentFacilitysRepository plplDepartmentFacilitysRepository;

    private final PLPLBlocksRepository blocksRepository;

    private final EmailService emailService;

    private final PlanNotificationRepository notificationRepository;

    private final PlanLogHistoryService planLogHistoryService;

    private final GoogleFormService googleFormService;

    @Value("${ws.topicPrefix}")
    private String topicPrefix;

    private final NotificationService notificationService;

    @Override
    public ResponseObject<?> getAllPlans(PLPLPlanListRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(plplPlansRepository.getAllPlanning(pageable, request)),
                HttpStatus.OK,
                "Lấy danh sách kế hoach thành công!"
        );
    }

    @Override
    public ResponseObject<?> createPlan(PLPLCreatePlanRequest request) {
        Optional<DepartmentFacility> departmentFacilityOptional = plplDepartmentFacilitysRepository
                .findByDepartment_CodeAndFacilityCode(
                        request.getDepartmentCode(),
                        request.getFacilityCode()
                );

        PlanLogHistoryRequest planLogHistory = new PlanLogHistoryRequest();
        planLogHistory.setTypeFunction(FunctionLogType.CREATE_PLAN);
        planLogHistory.setAction("Tạo kế hoạch");
        planLogHistory.setRoleStaff(Role.NGUOI_LAP_KE_HOACH.name());
        try {
            if (departmentFacilityOptional.isEmpty()) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Bộ môn cơ sở không tồn tại");
            }

            Optional<Block> blockOptional = blocksRepository.findById(request.getBlockId());
            if (blockOptional.isEmpty()) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Block không tồn tại");
            }

            Optional<Staff> staffOptional = plplStaffsRepository.findByStaffCode(request.getUserCode());
            Optional<Plan> planOptional = plplPlansRepository.findByBlockAndDepartmentFacility(blockOptional.get(), departmentFacilityOptional.get());
            if (planOptional.isPresent()) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch đã tồn tại");
            }
            if (staffOptional.isEmpty()) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Nhân viên không tồn tại");
            }
            Plan plan = new Plan();
            plan.setPlanStatus(PlanStatus.PLANNING);
            plan.setDepartmentFacility(departmentFacilityOptional.get());
            plan.setBlock(blockOptional.get());
            plan.setDescription(request.getDescription());
            plan.setPlanner(staffOptional.get());
            plan.setStartDate(request.getStartTime());
            plan.setEndDate(request.getEndTime());
            Plan planSaveResult = plplPlansRepository.save(plan);

            //Gửi thông báo đến TRUONG_MON và CHU_NHIEM_BO_MON
            Semester semesterNotification = planSaveResult.getBlock().getSemester();
            Block blockNotification = planSaveResult.getBlock();
            List<Notification> listNotificationSave = new ArrayList<>();
            for (String role : List.of(Role.TRUONG_MON.toString(), Role.CHU_NHIEM_BO_MON.toString())) {
                Notification item = new Notification();
                item.setPlan(planSaveResult);
                item.setContent(
                        "Kế hoạch tại " + BlockName.toString(blockNotification.getName()) + " của kì " +
                                semesterNotification.getSemesterName() + "-" + semesterNotification.getYear() +
                                " đã được tạo bởi người lập kế hoạch(" + staffOptional.get().getName() + ").");
                item.setStaff(staffOptional.get());
                item.setSentTo(role);
                listNotificationSave.add(item);
            }
            notificationRepository.saveAll(listNotificationSave);

            notificationService.sendNotification(
                    topicPrefix,
                    NotifyModel.getRoles(
                            Role.TRUONG_MON.name(),
                            Role.CHU_NHIEM_BO_MON.name()
                    )
            );

            planLogHistory.setDescription(request.getDescription());
            planLogHistory.setStartDate(request.getStartTime());
            planLogHistory.setEndDate(request.getEndTime());
            planLogHistory.setPlanId(planSaveResult.getId());
            planLogHistory.setStatus(true);
            return new ResponseObject<>(null, HttpStatus.CREATED, "Tạo kế hoạch thành công");
        } catch (Exception e) {
            planLogHistory.setStatus(false);
        } finally {
            try {
                Boolean resultLog = planLogHistoryService.createPlanLogHistory(planLogHistory);
                if (!resultLog) {
                    log.error("Có lỗi xảy ra khi lưu log");
                }
            } catch (Exception e) {
                log.error("Lỗi khi ghi log: {}", e.getMessage());
            }
        }
        return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Có lỗi sảy ra khi lưu kế hoạch");
    }

    @Override
    public ResponseObject<?> updatePlan(String planId, PLPLUpdatePlanRequest request) {
        Long canUpdate = plplPlansRepository.canUpdate(planId);
        PlanLogHistoryRequest planLogHistory = new PlanLogHistoryRequest();
        planLogHistory.setTypeFunction(FunctionLogType.UPDATE_PLAN);
        planLogHistory.setAction("Chỉnh sửa kế hoạch");
        planLogHistory.setRoleStaff(Role.NGUOI_LAP_KE_HOACH.name());
        try {
            if (canUpdate > 0) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Không thể sửa kế hoạch khi trưởng môn đã thêm môn tutor!");
            }

            Optional<Plan> planOptional = plplPlansRepository.findById(planId);
            if (planOptional.isEmpty()) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch không tồn tại");
            }

            Optional<Block> blockOptional = blocksRepository.findById(request.getBlockId());
            if (blockOptional.isEmpty()) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Block không tồn tại");
            }

            Plan plan = planOptional.get();
            plan.setPlanStatus(PlanStatus.PLANNING);
            plan.setBlock(blockOptional.get());
            plan.setDescription(request.getDescription());
            plan.setStartDate(request.getStartTime());
            plan.setEndDate(request.getEndTime());
            Plan planSaveResult = plplPlansRepository.save(plan);

            planLogHistory.setDescription(request.getDescription());
            planLogHistory.setStartDate(request.getStartTime());
            planLogHistory.setEndDate(request.getEndTime());
            planLogHistory.setPlanId(planSaveResult.getId());
            planLogHistory.setStatus(true);

            List<Notification> listNotificationSave = new ArrayList<>();
            Semester semesterNotification = planOptional.get().getBlock().getSemester();
            Block blockNotification = planOptional.get().getBlock();
            for (String role : List.of(Role.TRUONG_MON.toString(), Role.CHU_NHIEM_BO_MON.toString())) {
                Notification item = new Notification();
                item.setPlan(planSaveResult);
                item.setContent("Kế hoạch tại " + BlockName.toString(blockNotification.getName()) + " của kì " + semesterNotification.getSemesterName() + "-" + semesterNotification.getYear() + " đã được chỉnh sửa.");
                item.setStaff(planSaveResult.getPlanner());
                item.setSentTo(role);
                listNotificationSave.add(item);
            }
            notificationRepository.saveAll(listNotificationSave);

            notificationService.sendNotification(
                    NotifyModel.getRoles(
                            Role.TRUONG_MON.name(),
                            Role.CHU_NHIEM_BO_MON.name()
                    )
            );

            return new ResponseObject<>(null, HttpStatus.CREATED, "Sửa kế hoạch thành công");
        } catch (Exception e) {
            planLogHistory.setStatus(false);
            e.printStackTrace(System.out);
        } finally {
            try {
                Boolean resultLog = planLogHistoryService.createPlanLogHistory(planLogHistory);
                if (!resultLog) {
                    log.error("Có lỗi xảy ra khi lưu log");
                }
            } catch (Exception e) {
                log.error("Lỗi khi ghi log: {}", e.getMessage());
            }
        }
        return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Có lỗi sảy ra khi lưu cập nhật kế hoạch");
    }

    @Override
    public ResponseObject<?> getPlansDetail(String planId) {
        return plplPlansRepository.getPlanById(planId)
                .map(plan -> new ResponseObject<>(plan, HttpStatus.OK, "Lấy thông tin môn học thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Môn học không tồn tại"));
    }

    @Override
    public ResponseObject<?> getPlansBySemester(PLPLPlanInfoRequest request) {
        return plplPlansRepository.getSemesterInfo(request)
                .map(plan -> new ResponseObject<>(plan, HttpStatus.OK, "Lấy thông tin học kỳ thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Học kỳ không tồn tại"));
    }

    @Override
    public ResponseObject<?> getPlansInfo(PLPLPlanInfoRequest request) {
        return new ResponseObject<>(
                plplPlansRepository.getPlanInfo(request),
                HttpStatus.OK,
                "Lấy danh sách kế hoach thành công!"
        );
    }

    @Override
    public ResponseObject<?> getPlansInfoById(String id) {
        return plplPlansRepository.getPlanInfoById(id)
                .map(plan -> new ResponseObject<>(plan, HttpStatus.OK, "Lấy thông tin kế hoạch thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Kế hoạch không tồn tại"));
    }

    @Override
    public ResponseObject<?> approvePlan(String planId) {
        Optional<Plan> planOptional = plplPlansRepository.findById(planId);
        PlanLogHistoryRequest planLogHistory = new PlanLogHistoryRequest();
        planLogHistory.setTypeFunction(FunctionLogType.APPROVE_PLAN);
        planLogHistory.setAction("Phê duyệt kế hoạch");
        planLogHistory.setRoleStaff(Role.NGUOI_LAP_KE_HOACH.name());

        try {
            if (planOptional.isEmpty()) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch không tồn tại");
            }

            Long canUpdate = plplPlansRepository.canUpdate(planId);
            if (canUpdate == 0) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch này chưa có môn tutor!");
            }

            List<Notification> listNotificationSave = new ArrayList<>();
            Semester semesterNotification = planOptional.get().getBlock().getSemester();
            Block blockNotification = planOptional.get().getBlock();
            Staff staffNotification = planOptional.get().getPlanner();
            for (String role : List.of(Role.TRUONG_MON.toString(), Role.CHU_NHIEM_BO_MON.toString())) {
                Notification item = new Notification();
                item.setPlan(planOptional.get());
                item.setContent(
                        "Kế hoạch tại " + BlockName.toString(blockNotification.getName()) + " của kì " +
                                semesterNotification.getSemesterName() + "-" + semesterNotification.getYear() +
                                " đã được phê duyệt bởi người lập kế hoạch(" + staffNotification.getName() + ").");
                item.setStaff(staffNotification);
                item.setSentTo(role);
                listNotificationSave.add(item);
            }
            notificationRepository.saveAll(listNotificationSave);

            notificationService.sendNotification(
                    topicPrefix,
                    NotifyModel.getRoles(
                            Role.TRUONG_MON.name(),
                            Role.CHU_NHIEM_BO_MON.name()
                    )
            );

            planOptional.map(plan -> {
                plan.setPlanStatus(PlanStatus.PLANNER_APPROVED);
                Plan planSaveResult = plplPlansRepository.save(plan);
                planLogHistory.setPlanId(planSaveResult.getId());
                planLogHistory.setStatus(true);
                return planSaveResult;
            });

            Department department = planOptional.get().getDepartmentFacility().getDepartment();
            Facility facility = planOptional.get().getDepartmentFacility().getFacility();
            Block block = planOptional.get().getBlock();
            Semester semester = block.getSemester();
            List<String> list = plplStaffsRepository.getStaffsByRole(Arrays.asList("CHU_NHIEM_BO_MON", "TRUONG_MON"), department.getCode(), facility.getCode());
            String blockName = block.getName().name().equals("BLOCK_1") ? "Block 1" : "Block 2";
            String message = "Kế hoạch học kì " + semester.getSemesterName() + " " + semester.getYear() + " " + blockName + " đã được người lập kế hoạch phê duyệt. Chủ nhiệm bộ môn/Trưởng môn vui lòng hãy kiểm tra và theo dõi kế hoạch tutor .";
            emailService.sendEmailToHeadSubjectAboutPlan(message, list);

            return planOptional
                    .map(plan -> new ResponseObject<>(null, HttpStatus.OK, "Cập nhật thành công"))
                    .orElseGet(() -> new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Cập nhật thất bại"));
        } catch (Exception e) {
            planLogHistory.setStatus(false);
        } finally {
            try {
                Boolean resultLog = planLogHistoryService.createPlanLogHistory(planLogHistory);
                if (!resultLog) {
                    log.error("Có lỗi xảy ra khi lưu log");
                }
            } catch (Exception e) {
                log.error("Lỗi khi ghi log: {}", e.getMessage());
            }
        }
        return new ResponseObject<>(null, HttpStatus.OK, "Phê duyệt kế hoạch thành công");
    }

    @Override
    public ResponseObject<?> checkApprovePlan(String planId) {

        Plan planOptional = plplPlansRepository.findById(planId).orElse(null);

        if (planOptional == null) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch không tồn tại");
        }

        Long currentDateMillis = System.currentTimeMillis();
        if (planOptional.getStartDate() > currentDateMillis || planOptional.getEndDate() < currentDateMillis) {
            return new ResponseObject<>(
                    false,
                    HttpStatus.OK,
                    "Lấy danh sách kế hoach thành công!"
            );
        }
        return new ResponseObject<>(
                true,
                HttpStatus.OK,
                "Lấy danh sách kế hoach thành công!"
        );
    }

    @Override
    public ResponseObject<?> startPlan(String planId) {
        // Tìm kế hoạch theo ID
        Plan planOptional = plplPlansRepository.findById(planId).orElse(null);
        if (planOptional == null) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch không tồn tại");
        }

        // Tạo Google Form và nhận phản hồi
        ResponseObject<?> formResponse = googleFormService.createForm(planOptional);
        if (formResponse.getStatus() != HttpStatus.OK) {
            return formResponse; // Nếu có lỗi, trả về phản hồi lỗi từ createForm
        }

        // Trả về phản hồi thành công
        return new ResponseObject<>(null, HttpStatus.OK, "Chuyển trạng thái kế hoạch thành công, URL Google Form: " + formResponse.getData());
    }

}
