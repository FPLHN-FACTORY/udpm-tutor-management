package udpm.hn.server.core.planloghistory.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.planloghistory.model.request.PlanLogHistoryFilterRequest;
import udpm.hn.server.core.planloghistory.model.request.PlanLogHistoryRequest;
import udpm.hn.server.core.planloghistory.model.response.PlanLogHistoryResponse;
import udpm.hn.server.core.planloghistory.repository.PLSPlanLogHistoryRepository;
import udpm.hn.server.core.planloghistory.service.PlanLogHistoryService;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.entity.PlanLogHistory;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.PlanRepository;
import udpm.hn.server.repository.StaffRepository;
import udpm.hn.server.utils.Helper;
import udpm.hn.server.utils.UserContextHelper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class PlanLogHistoryServiceImpl implements PlanLogHistoryService {

    private final PlanRepository planRepository;
    private final StaffRepository staffRepository;
    private final PLSPlanLogHistoryRepository planLogHistoryRepository;

    @Override
    public Boolean createPlanLogHistory(PlanLogHistoryRequest request) {
        try {
            PlanLogHistory planLogHistory = new PlanLogHistory();
            String userId = UserContextHelper.getCurrentUserId();
            if (userId != null) {
                staffRepository.findById(userId).ifPresent(staff -> {
                    planLogHistory.setUserName(staff.getName());
                    planLogHistory.setCodeStaff(staff.getStaffCode());
                    planLogHistory.setEmail(staff.getEmailFpt());
                });
            }
            if(request.getPlanId()!=null) {
                Optional<Plan> plan = planRepository.findById(request.getPlanId());
                if(plan.isPresent()) {
                    planLogHistory.setPlan(plan.get().getId());
                }
            }
            if(request.getStatus()) {
                planLogHistory.setStatus(EntityStatus.ACTIVE);
            } else {
                planLogHistory.setStatus(EntityStatus.INACTIVE);
            }
            planLogHistory.setRoomPlan(request.getRoomPlan());
            planLogHistory.setStudentTutor(request.getStudentTutor());
            planLogHistory.setRejectNote(request.getRejectNote());
            planLogHistory.setStaffInfo(request.getStaffInfo());
            planLogHistory.setCodeTutorClassDetail(request.getCodeTutorClassDetail());
            planLogHistory.setNumberOfClass(request.getNumberOfClass());
            planLogHistory.setNumberOfLecture(request.getNumberOfLecture());
            planLogHistory.setFormality(request.getFormality());
            planLogHistory.setStartDate(request.getStartDate());
            planLogHistory.setEndDate(request.getEndDate());
            planLogHistory.setDescription(request.getDescription());
            planLogHistory.setAction(request.getAction());
            planLogHistory.setTypeFunction(request.getTypeFunction());
            planLogHistory.setTimestamp(System.currentTimeMillis());
            planLogHistory.setCreatedDate(System.currentTimeMillis());
            planLogHistory.setRoleStaff(request.getRoleStaff());
            planLogHistoryRepository.save(planLogHistory);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ResponseObject<?> getAllPlanLogHistory(PlanLogHistoryFilterRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        Page<PlanLogHistoryResponse> logSystemResponses = planLogHistoryRepository.getAllPlanLogHistory(request, pageable);

        return new ResponseObject<>(
                PageableObject.of(logSystemResponses),
                HttpStatus.OK,
                "Lấy thành công danh sách lịch sử kế hoạch của trưởng môn"
        );
    }

    @Override
    public ResponseObject<?> getDetailPlanLogHistory(String id) {
        return new ResponseObject<>(planLogHistoryRepository.getPlanLogHistoryById(id), HttpStatus.OK, "Lấy thành công chi tiết log");
    }
}
