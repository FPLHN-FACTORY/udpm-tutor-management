package udpm.hn.server.core.headsubject.plan.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLCreateTutorClassRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLSubjectListRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLTutorClassDetailRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLUpdateTutorClassDetailRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLUpdateTutorClassRequest;
import udpm.hn.server.core.headsubject.plan.model.response.HSPLTutorClassResponse;
import udpm.hn.server.core.headsubject.plan.repository.HSPLNotificationRepository;
import udpm.hn.server.core.headsubject.plan.repository.HSPLPlansRepository;
import udpm.hn.server.core.headsubject.plan.repository.HSPLStaffsRepository;
import udpm.hn.server.core.headsubject.plan.repository.HSPLSubjectRepository;
import udpm.hn.server.core.headsubject.plan.repository.HSPLTutorClassDetailRepository;
import udpm.hn.server.core.headsubject.plan.repository.HSPLTutorClassRepository;
import udpm.hn.server.core.headsubject.plan.service.HSPLTutorClassService;
import udpm.hn.server.core.superadmin.operation.model.request.OperationLogsRequest;
import udpm.hn.server.core.superadmin.operation.service.OperationLogsService;
import udpm.hn.server.core.planloghistory.model.request.PlanLogHistoryRequest;
import udpm.hn.server.core.planloghistory.service.PlanLogHistoryService;
import udpm.hn.server.entity.Notification;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.Subject;
import udpm.hn.server.entity.TutorClass;
import udpm.hn.server.entity.TutorClassDetail;
import udpm.hn.server.infrastructure.config.websocket.model.NotifyModel;
import udpm.hn.server.infrastructure.config.websocket.service.NotificationService;
import udpm.hn.server.infrastructure.constant.Format;
import udpm.hn.server.infrastructure.constant.FunctionLogType;
import udpm.hn.server.infrastructure.constant.Role;
import udpm.hn.server.utils.Helper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class HSPLTutorClassServiceImpl implements HSPLTutorClassService {

    private final HSPLStaffsRepository staffRepository;

    private final HSPLSubjectRepository subjectRepository;

    private final HSPLPlansRepository planRepository;

    private final HSPLTutorClassRepository tutorClassRepository;

    private final HSPLTutorClassDetailRepository tutorClassDetailRepository;

    private final OperationLogsService operationLogsService;

    private final PlanLogHistoryService planLogHistoryService;

    private final HSPLNotificationRepository notificationRepository;

    private final NotificationService notificationService;

    @Value("${ws.topicPrefix}")
    private String topicPrefix;

    @Override
    public ResponseObject<?> createTutorClass(HSPLCreateTutorClassRequest request) {
        OperationLogsRequest operationLogsRequest = new OperationLogsRequest();
        PlanLogHistoryRequest planLogHistory = new PlanLogHistoryRequest();
        planLogHistory.setTypeFunction(FunctionLogType.ADD_NUMBER_OF_CLASS);
        planLogHistory.setAction("Tạo lớp tutor");
        planLogHistory.setRoleStaff(Role.TRUONG_MON.name());
        try {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            operationLogsRequest.setHttpRequest(httpServletRequest);
            operationLogsRequest.setTypeFunction(FunctionLogType.CREATE);

            Plan plan = planRepository.findById(request.getPlanId()).orElse(null);
            Subject subject = subjectRepository.findById(request.getSubjectId()).orElse(null);
            TutorClass tutorClass = new TutorClass();
            tutorClass.setPlan(plan);
            tutorClass.setSubject(subject);
            tutorClass.setNumberOfClasses(request.getNumberOfClasses());
            tutorClass.setNumberOfLectures(request.getNumberOfLectures());
            tutorClass.setFormat(Format.fromString(request.getFormat()));
            operationLogsRequest.setRequest(tutorClass);
            operationLogsRequest.setStatus(true);
            operationLogsRequest.setResponse("{\"status\": \"true\"}");
            TutorClass savedTutorClass = tutorClassRepository.save(tutorClass);
            String code = subject.getCode();
            if (savedTutorClass == null) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Có lỗi sảy ra khi cập nhật lớp toutor");
            }
            if (plan != null && plan.getId() != null) {
                planLogHistory.setPlanId(plan.getId());
            }
            planLogHistory.setFormality(Format.fromString(request.getFormat()));
            planLogHistory.setNumberOfClass(request.getNumberOfClasses());
            planLogHistory.setNumberOfLecture(request.getNumberOfLectures());
            planLogHistory.setStatus(true);

            // Lưu tutor detail dựa vào số lương
            if (tutorClass.getNumberOfClasses() != null && tutorClass.getNumberOfClasses() > 0) {
                for (int i = 0; i < tutorClass.getNumberOfClasses(); i++) {
                    TutorClassDetail tutorClassDetail = new TutorClassDetail();
                    tutorClassDetail.setTutorClass(savedTutorClass);
                    tutorClassDetail.setName(code + " lớp " + (i + 1));
                    tutorClassDetail.setNumberOfLectures(request.getNumberOfLectures());
                    tutorClassDetail.setCode(Helper.generateTutorClassCodeFromName(code));
                    tutorClassDetailRepository.save(tutorClassDetail);
                }
            }
            //Thêm 2 thông báo đến NGUOI_LAP_KE_HOACH và CHU_NHIEM_BO_MON
            List<Notification> listNotificationSave = new ArrayList<>();
            for (String role : List.of(Role.NGUOI_LAP_KE_HOACH.toString(), Role.CHU_NHIEM_BO_MON.toString())) {
                Notification item = new Notification();
                item.setPlan(plan);
                item.setContent("Kế hoạch " + plan.getDescription() + " đã được trưởng môn thêm lớp tutor.");
                item.setStaff(plan.getPlanner());
                item.setSentTo(role);
                listNotificationSave.add(item);
            }
            notificationRepository.saveAll(listNotificationSave);

            notificationService.sendNotification(
                    topicPrefix,
                    NotifyModel.getRoles(
                            Role.NGUOI_LAP_KE_HOACH.name(),
                            Role.CHU_NHIEM_BO_MON.name()
                    )
            );


            return new ResponseObject<>(tutorClass, HttpStatus.CREATED, "Tạo mới lớp tutor thành công");
        } catch (Exception e) {
            operationLogsRequest.setStatus(false);
            planLogHistory.setStatus(false);
            operationLogsRequest.setResponse("{\"status\": \"false\"}");
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cập nhật số lượng lớp tutor không thành công: " + e.getMessage());
        } finally {
            operationLogsService.createOperationLog(operationLogsRequest);
            try {
                Boolean resultLog = planLogHistoryService.createPlanLogHistory(planLogHistory);
                if (!resultLog) {
                    System.err.println("Có lỗi xảy ra khi lưu log");
                }
            } catch (Exception e) {
                System.err.println("Lỗi khi ghi log: " + e.getMessage());
            }
        }
    }

    @Override
    public ResponseObject<?> updateTutorClass(String id, HSPLUpdateTutorClassRequest request) {
        PlanLogHistoryRequest planLogHistory = new PlanLogHistoryRequest();
        planLogHistory.setTypeFunction(FunctionLogType.UPDATE);
        planLogHistory.setAction("Cập nhật lớp tutor");
        planLogHistory.setRoleStaff(Role.TRUONG_MON.name());
        try {
            TutorClass tutorClass = tutorClassRepository.findById(id).orElse(null);
            if (tutorClass == null) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Id không hợp lệ");
            }
            tutorClass.setFormat(Format.fromString(request.getFormat()));
            tutorClass.setNumberOfLectures(request.getNumberOfLectures());
            TutorClass tutorClassResult = tutorClassRepository.save(tutorClass);
            if (tutorClassResult == null) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Có lỗi sảy ra khi cập nhật lớp tutor");
            }
            planLogHistory.setPlanId(tutorClassResult.getPlan().getId());
            planLogHistory.setStatus(true);
            return new ResponseObject<>(tutorClass, HttpStatus.OK, "Cập nhật trạng thái lớp tutor thành công");
        } catch (Exception e) {
            planLogHistory.setStatus(false);
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cập nhật trạng thái lớp tutor không thành công: " + e.getMessage());
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
    }

    @Override
    public ResponseObject<?> getDetailTutorClass(String id) {
        try {
            HSPLTutorClassResponse tutorClass = tutorClassRepository.getDetailTutorClass(id);
            if (tutorClass == null) {
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Không tồn tại");
            }
            return new ResponseObject<>(tutorClass, HttpStatus.OK, "Lấy lớp tutor thành công1");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lỗi khi lấy chi tiết: " + e.getMessage());
        }
    }

    @Override
    public ResponseObject<?> getTutorClasses(HSPLSubjectListRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(tutorClassRepository.getTutorClasses(pageable, request)
                ), HttpStatus.OK, "Lấy danh sách lớp môn thành công!");
    }

    @Override
    public ResponseObject<?> getTutorClassDetail(HSPLTutorClassDetailRequest request) {
        if (request.getQuery() != null) {
            request.setQuery(request.getQuery().trim());
        }
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(tutorClassDetailRepository.getTutorClassDetailByTutorClassId(pageable, request)),
                HttpStatus.OK,
                "Lấy danh sách lớp môn thành công!"
        );
    }

    @Override
    public ResponseObject<?> updateTutorClassDetail(String id, HSPLUpdateTutorClassDetailRequest request) {
        PlanLogHistoryRequest planLogHistory = new PlanLogHistoryRequest();
        planLogHistory.setTypeFunction(FunctionLogType.ADD_STAFF_OF_CLASS_TUTOR_DETAIL);
        planLogHistory.setAction("Thêm giảng viên tutor");
        planLogHistory.setRoleStaff(Role.TRUONG_MON.name());
        try {
            TutorClassDetail tutorClassDetail = tutorClassDetailRepository.findById(id).orElse(null);
            if (tutorClassDetail == null) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lớp tutor không tồn tại!");
            }

            Staff staff = staffRepository.findById(request.getStaffId()).orElse(null);
            if (staff == null) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Giáo viên không tồn tại!");
            }
            tutorClassDetail.setTeacherConduct(staff);
            TutorClassDetail tutorClassDetailResult = tutorClassDetailRepository.save(tutorClassDetail);
            if (tutorClassDetailResult == null) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Có lỗi sảy ra khi cập nhật chi tiết lớp tutor");
            }
            planLogHistory.setStaffInfo(staff.getStaffCode() + "-" + staff.getName());
            planLogHistory.setCodeTutorClassDetail(tutorClassDetailResult.getCode());
            planLogHistory.setPlanId(tutorClassDetailResult.getTutorClass().getPlan().getId());
            planLogHistory.setStatus(true);

            //Thêm 2 thông báo đến GIANG_VIEN
            List<Notification> listNotificationSave = new ArrayList<>();
            for (String role : List.of(Role.GIANG_VIEN.toString())) {
                Notification item = new Notification();
                item.setPlan(tutorClassDetailResult.getTutorClass().getPlan());
                item.setContent("Bạn đã được thêm vào lớp " + tutorClassDetailResult.getCode() + ".");
                item.setStaff(staff);
                item.setSentTo(role + ": " + staff.getId());
                listNotificationSave.add(item);
            }
            notificationRepository.saveAll(listNotificationSave);

            return new ResponseObject<>(tutorClassDetail, HttpStatus.OK, "Cập nhật giáo viên lớp tutor thành công");
        } catch (Exception e) {
            planLogHistory.setStatus(false);
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cập nhật giáo viên lớp tutor không thành công: " + e.getMessage());
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
    }

    @Override
    public ResponseObject<?> deleteTutorClassDetail(String id) {
        PlanLogHistoryRequest planLogHistory = new PlanLogHistoryRequest();
        planLogHistory.setTypeFunction(FunctionLogType.DELETE_TUTOR_CLASS_DETAIL);
        planLogHistory.setAction("Xóa chi tiết lớp tutor");
        planLogHistory.setRoleStaff(Role.TRUONG_MON.name());
        try {
            TutorClassDetail tutorClassDetail = tutorClassDetailRepository.findById(id).orElse(null);
            if (tutorClassDetail == null) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lớp tutor không tồn tại!");
            }
            planLogHistory.setCodeTutorClassDetail(tutorClassDetail.getCode());
            tutorClassDetailRepository.delete(tutorClassDetail);
            TutorClass tutorClass = tutorClassDetail.getTutorClass();
            if (tutorClass != null) {
                planLogHistory.setPlanId(tutorClass.getPlan().getId());
            }
            List<TutorClassDetail> list = tutorClassDetailRepository.findByTutorClass(tutorClass);
            if (list.isEmpty()) {
                tutorClassRepository.delete(tutorClass);
            }
            planLogHistory.setStatus(true);
            return new ResponseObject<>(tutorClassDetail, HttpStatus.OK, "Xóa lớp tutor thành công");
        } catch (Exception e) {
            planLogHistory.setStatus(false);
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Xóa lớp tutor thất bại: " + e.getMessage());
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
    }

    @Override
    public ResponseObject<?> addTutorClassDetail(String id) {
        PlanLogHistoryRequest planLogHistory = new PlanLogHistoryRequest();
        planLogHistory.setTypeFunction(FunctionLogType.ADD_TUTOR_CLASS_DETAIL);
        planLogHistory.setAction("Thêm chi tiết lớp tutor");
        planLogHistory.setRoleStaff(Role.TRUONG_MON.name());
        try {
            TutorClassDetail tutorClassDetail = tutorClassDetailRepository.findById(id).orElse(null);
            if (tutorClassDetail == null) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Lớp tutor không tồn tại!");
            }

            TutorClassDetail newTutorClassDetail = new TutorClassDetail();
            newTutorClassDetail.setTutorClass(tutorClassDetail.getTutorClass());
            newTutorClassDetail.setCode(Helper.generateTutorClassCodeFromName(tutorClassDetail.getTutorClass().getSubject().getCode()));
            TutorClassDetail newTutorClassDetailResult = tutorClassDetailRepository.save(newTutorClassDetail);
            if (newTutorClassDetailResult == null) {
                planLogHistory.setStatus(false);
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Có lỗi sảy ra khi thêm chi tiết lớp tutor");
            }
            planLogHistory.setCodeTutorClassDetail(newTutorClassDetailResult.getCode());
            planLogHistory.setPlanId(newTutorClassDetailResult.getTutorClass().getPlan().getId());
            planLogHistory.setStatus(true);
            return new ResponseObject<>(tutorClassDetail, HttpStatus.OK, "Thêm lớp tutor thành công");
        } catch (Exception e) {
            planLogHistory.setStatus(false);
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Thm lớp tutor thất bại: " + e.getMessage());
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
    }
}
