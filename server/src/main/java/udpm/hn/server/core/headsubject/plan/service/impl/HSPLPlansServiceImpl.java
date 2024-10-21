package udpm.hn.server.core.headsubject.plan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLPlanInfoRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLPlanListRequest;
import udpm.hn.server.core.headsubject.plan.repository.HSPLPlansRepository;
import udpm.hn.server.core.headsubject.plan.repository.HSPLStaffsRepository;
import udpm.hn.server.core.headsubject.plan.service.HSPLPlanService;
import udpm.hn.server.core.planner.plan.model.request.PLPLTutorListRequest;
import udpm.hn.server.repository.DepartmentFacilityRepository;
import udpm.hn.server.repository.DepartmentRepository;
import udpm.hn.server.repository.FacilityRepository;
import udpm.hn.server.repository.MajorFacilityRepository;
import udpm.hn.server.repository.MajorRepository;
import udpm.hn.server.repository.RoleRepository;
import udpm.hn.server.repository.StaffMajorFacilityRepository;
import udpm.hn.server.repository.StaffRepository;
import udpm.hn.server.repository.StaffRoleRepository;
import udpm.hn.server.utils.Helper;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class HSPLPlansServiceImpl implements HSPLPlanService {

    private final HSPLPlansRepository plansRepository;

    private final HSPLStaffsRepository hsplStaffsRepository;

    private final StaffRoleRepository staffRoleRepository;

    private final StaffRepository staffRepository;

    private final RoleRepository roleRepository;

    private final DepartmentFacilityRepository departmentFacilityRepository;

    private final MajorRepository majorRepository;

    private final DepartmentRepository departmentRepository;

    private final FacilityRepository facilityRepository;

    private final MajorFacilityRepository majorFacilityRepository;

    private final StaffMajorFacilityRepository staffMajorFacilityRepository;

    @Override
    public ResponseObject<?> getAllPlans(HSPLPlanListRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(plansRepository.getAllPlanning(pageable, request)),
                HttpStatus.OK,
                "Lấy danh sách kế hoach thành công!"
        );
    }

    @Override
    public ResponseObject<?> getPlansDetail(String planId) {
        return plansRepository.getPlanById(planId)
                .map(plan -> new ResponseObject<>(plan, HttpStatus.OK, "Lấy thông tin môn học thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Môn học không tồn tại"));
    }

    @Override
    public ResponseObject<?> getPlansBySemester(HSPLPlanInfoRequest request) {
        return plansRepository.getSemesterInfo(request)
                .map(plan -> new ResponseObject<>(plan, HttpStatus.OK, "Lấy thông tin học kỳ thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Học kỳ không tồn tại"));
    }

    @Override
    public ResponseObject<?> getPlansInfo(HSPLPlanInfoRequest request) {
        return new ResponseObject<>(
                plansRepository.getPlanInfo(request),
                HttpStatus.OK,
                "Lấy danh sách kế hoach thành công!"
        );
    }

    @Override
    public ResponseObject<?> getPlansInfoById(String id) {
        return plansRepository.getPlanInfoById(id)
                .map(plan -> new ResponseObject<>(plan, HttpStatus.OK, "Lấy thông tin kế hoạch thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Kế hoạch không tồn tại"));
    }

}
