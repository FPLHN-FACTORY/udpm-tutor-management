package udpm.hn.server.core.planner.plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.planner.plan.model.request.PLPLCreatePlanRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLPlanInfoRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLPlanListRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLTutorListRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLUpdatePlanRequest;
import udpm.hn.server.core.planner.plan.repository.PLPLBlocksRepository;
import udpm.hn.server.core.planner.plan.repository.PLPLDepartmentFacilitysRepository;
import udpm.hn.server.core.planner.plan.repository.PLPLPlansRepository;
import udpm.hn.server.core.planner.plan.repository.PLPLStaffsRepository;
import udpm.hn.server.core.planner.plan.repository.PLPLTutorClassRepository;
import udpm.hn.server.core.planner.plan.service.PLPLPlansService;
import udpm.hn.server.entity.Block;
import udpm.hn.server.entity.DepartmentFacility;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.constant.PlanStatus;
import udpm.hn.server.utils.Helper;

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
//    private final EmailService emailService;
    private final PLPLTutorClassRepository plplTutorClassRepository;

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
        Optional<DepartmentFacility> departmentFacilityOptional = plplDepartmentFacilitysRepository.findByDepartment_CodeAndFacilityCode(request.getDepartmentCode(),request.getFacilityCode());
        if (departmentFacilityOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Bộ môn cơ sở không tồn tại tồn tại");
        }

        Optional<Block> blockOptional = blocksRepository.findById(request.getBlockId());
        if (blockOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Block không tồn tại");
        }

        Optional<Staff> staffOptional = plplStaffsRepository.findByStaffCode(request.getUserCode());
        Optional<Plan> planOptional = plplPlansRepository.findByBlockAndDepartmentFacility(blockOptional.get(), departmentFacilityOptional.get());
        if(planOptional.isPresent()){
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch đã tồn tại");
        }
        Plan plan = new Plan();
        plan.setPlanStatus(PlanStatus.PLANNING);
        plan.setDepartmentFacility(departmentFacilityOptional.get());
        plan.setBlock(blockOptional.get());
        plan.setDescription(request.getDescription());
        plan.setPlanner(staffOptional.get());
        plplPlansRepository.save(plan);

//        Department department = departmentFacilityOptional.get().getDepartment();
//        Facility facility = departmentFacilityOptional.get().getFacility();
//        List<String> list = plplStaffsRepository.getAllHeadSubjectsByDepartment(department.getCode(), facility.getCode(),request.getSemesterId());
//        emailService.sendEmailToHeadSubjectAboutPlan("Kế hoạch học kì xxx block xxx đã được tạo. Trưởng môn hãy kiểm tra và xếp môn tutor cho học kỳ xxx block xxx",list);

        return new ResponseObject<>(null, HttpStatus.CREATED, "Tạo kế hoạch thành công");
    }

    @Override
    public ResponseObject<?> updatePlan(String planId, PLPLUpdatePlanRequest request) {
        Long canUpdate = plplPlansRepository.canUpdate(planId);
        if(canUpdate > 0){
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Không thể sửa kế hoạch khi trưởng môn đã thêm môn tutor!");
        }

        Optional<Plan> planOptional = plplPlansRepository.findById(planId);
        if(planOptional.isEmpty()){
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch không tồn tại");
        }

        Optional<Block> blockOptional = blocksRepository.findById(request.getBlockId());
        if (blockOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Block không tồn tại");
        }

        Plan plan = planOptional.get();
        plan.setPlanStatus(PlanStatus.PLANNING);
        plan.setBlock(blockOptional.get());
        plan.setDescription(request.getDescription());
        plplPlansRepository.save(plan);

        return new ResponseObject<>(null, HttpStatus.CREATED, "Sửa kế hoạch thành công");
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

        if (planOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch không tồn tại");
        }

        Long canUpdate = plplPlansRepository.canUpdate(planId);
        if(canUpdate == 0){
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Kế hoạch này chưa có môn tutor!");
        }

        planOptional.map(plan -> {
            plan.setPlanStatus(PlanStatus.PLANNER_APPROVED);
            return plplPlansRepository.save(plan);
        });

        return planOptional
                .map(plan -> new ResponseObject<>(null, HttpStatus.OK, "Cập nhật thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Cập nhật thất bại"));
    }
}
