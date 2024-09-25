package udpm.hn.server.core.admin.departments.departmentfacility.service.impl;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.CreateOrUpdateDepartmentFacilityRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.FindFacilityDetailRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.repository.DFDepartmentExtendRepository;
import udpm.hn.server.core.admin.departments.departmentfacility.repository.DFDepartmentFacilityExtendRepository;
import udpm.hn.server.core.admin.departments.departmentfacility.repository.DFFacilityExtendRepository;
import udpm.hn.server.core.admin.departments.departmentfacility.repository.DFStaffExtendRepository;
import udpm.hn.server.core.admin.departments.departmentfacility.service.DepartmentFacilityService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Department;
import udpm.hn.server.entity.DepartmentFacility;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.entity.MajorFacility;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.connection.IdentityConnection;
import udpm.hn.server.infrastructure.connection.response.DepartmentCampusResponse;
import udpm.hn.server.infrastructure.connection.response.MajorCampusResponse;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.DepartmentFacilityRepository;
import udpm.hn.server.repository.DepartmentRepository;
import udpm.hn.server.repository.FacilityRepository;
import udpm.hn.server.repository.StaffRepository;
import udpm.hn.server.utils.Helper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class DepartmentFacilityServiceImpl implements DepartmentFacilityService {

    private final DFDepartmentFacilityExtendRepository dfDepartmentFacilityExtendRepository;

    private final DFDepartmentExtendRepository dfDepartmentExtendRepository;

    private final DFFacilityExtendRepository dfFacilityExtendRepository;

    private final DFStaffExtendRepository dfStaffExtendRepository;

    private final IdentityConnection identityConnection;

    private final DepartmentFacilityRepository departmentFacilityRepository;

    private final DepartmentRepository departmentRepository;

    private final FacilityRepository facilityRepository;

    private final StaffRepository staffRepository;

    @Override
    public ResponseObject<?> getAllDepartmentFacility(String id, FindFacilityDetailRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(dfDepartmentFacilityExtendRepository.getDepartmentFacilitiesByValueFind(id, pageable, request)),
                HttpStatus.OK,
                "Lấy thành công danh sách bộ môn theo cơ sở"
        );
    }

    @Override
    public ResponseObject<?> addDepartmentFacility(@Valid CreateOrUpdateDepartmentFacilityRequest request) {
        Optional<Department> departmentOptional = dfDepartmentExtendRepository.findById(request.getDepartmentId());
        Optional<Facility> facilityOptional = dfFacilityExtendRepository.findById(request.getFacilityId());
        Optional<Staff> staffOptional = dfStaffExtendRepository.findById(request.getHeadOfDepartmentId());

        if (dfDepartmentFacilityExtendRepository.existsByIdDepartmentAndIdFacilityAndIdAdd(request).isPresent()) {
            return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Bộ môn theo cơ sở đã tồn tại!");
        }

        if (facilityOptional.isPresent() && staffOptional.isPresent() && departmentOptional.isPresent()) {
            DepartmentFacility addDepartmentFacility = new DepartmentFacility();
            addDepartmentFacility.setDepartment(departmentOptional.get());
            addDepartmentFacility.setFacility(facilityOptional.get());
            addDepartmentFacility.setStaff(staffOptional.get());
            addDepartmentFacility.setStatus(EntityStatus.ACTIVE);
            dfDepartmentFacilityExtendRepository.save(addDepartmentFacility);
            return new ResponseObject<>(null, HttpStatus.CREATED, "Thêm thành công");
        } else if (facilityOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Không tìm thấy cơ sở trên");
        } else if (staffOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Không tìm thấy CNBM trên");
        } else {
            return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Không tìm thấy bộ môn trên");
        }
    }

    @Override
    public ResponseObject<?> updateDepartmentFacility(@Valid CreateOrUpdateDepartmentFacilityRequest request, String id) {
        Optional<DepartmentFacility> departmentFacilityOptional = dfDepartmentFacilityExtendRepository.findById(id);
        if (departmentFacilityOptional.isPresent()) {
            Optional<Department> departmentOptional = dfDepartmentExtendRepository.findById(request.getDepartmentId());
            Optional<Facility> facilityOptional = dfFacilityExtendRepository.findById(request.getFacilityId());
            Optional<Staff> staffOptional = dfStaffExtendRepository.findById(request.getHeadOfDepartmentId());

            if (facilityOptional.isPresent() && staffOptional.isPresent() && departmentOptional.isPresent()) {
                DepartmentFacility updateDepartmentFacility = departmentFacilityOptional.get();
                if (updateDepartmentFacility.getFacility().equals(facilityOptional.get())) {
                    updateDepartmentFacility.setStaff(staffOptional.get());
                    updateDepartmentFacility.setDepartment(departmentOptional.get());
                    updateDepartmentFacility.setFacility(facilityOptional.get());
                    dfDepartmentFacilityExtendRepository.save(updateDepartmentFacility);
                } else {
                    if (isDuplicateRecord(request).isPresent()) {
                        return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Cập nhật trùng cơ sở hoặc để như cũ");
                    }
                    updateDepartmentFacility.setStaff(staffOptional.get());
                    updateDepartmentFacility.setDepartment(departmentOptional.get());
                    updateDepartmentFacility.setFacility(facilityOptional.get());
                    dfDepartmentFacilityExtendRepository.save(updateDepartmentFacility);
                }

                return new ResponseObject<>(null, HttpStatus.OK, "Sửa thành công !");
            } else if (facilityOptional.isEmpty()) {
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Không tìm thấy cơ sở trên");
            } else if (staffOptional.isEmpty()) {
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Không tìm thấy nhân viên trên");
            } else if (departmentOptional.isEmpty()) {
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Không tìm thấy bộ môn trên");
            } else {
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Sửa thất bại");
            }
        } else {
            return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Bộ môn theo cơ sở trên không tồn tại");
        }
    }

    private Optional<DepartmentFacility> isDuplicateRecord(CreateOrUpdateDepartmentFacilityRequest request) {
        return dfDepartmentFacilityExtendRepository.existsDepartmentFacilitiesByDepartmentAndFacility(request.getDepartmentId(), request.getFacilityId());
    }

    @Override
    public ResponseObject<?> deleteDepartmentFacility(String id) {
        Optional<DepartmentFacility> departmentFacilityOptional = dfDepartmentFacilityExtendRepository.findById(id);
        if (departmentFacilityOptional.isPresent()) {
            DepartmentFacility departmentFacility = departmentFacilityOptional.get();
            departmentFacility.setStatus(
                    departmentFacility.getStatus().name().equalsIgnoreCase(EntityStatus.ACTIVE.name()) ? EntityStatus.INACTIVE : EntityStatus.ACTIVE
            );
            dfDepartmentFacilityExtendRepository.save(departmentFacility);
            return new ResponseObject<>(null, HttpStatus.OK, "Chỉnh sửa thành công bộ môn theo cơ sở");
        }

        return new ResponseObject<>(null, HttpStatus.OK, "Bộ môn theo cơ sở không tồn tại");
    }

    @Override
    public ResponseObject<?> getListFacility() {
        return new ResponseObject<>(dfDepartmentFacilityExtendRepository.getListFacility(), HttpStatus.OK, "Lấy thành công danh sách cơ sở");
    }

    @Override
    public ResponseObject<?> getListStaff() {
        return new ResponseObject<>(dfDepartmentFacilityExtendRepository.getListStaff(), HttpStatus.OK, "Lấy thành công danh sách CNBM");
    }

    @Override
    public ResponseObject<?> getDepartmentName(String departmentId) {
        Optional<Department> departmentOptional = dfDepartmentExtendRepository.findById(departmentId);
        if (departmentOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Tìm thấy bộ môn này!");
        }
        return new ResponseObject<>(departmentOptional.get().getName(), HttpStatus.OK, "Tìm thấy thành công tên bộ môn");
    }


    @Override
    @Transactional
    public ResponseObject<?> synchronize() {
        try {
            List<DepartmentCampusResponse> departmentCampusData = identityConnection.getDepartmentCampuses();
            List<DepartmentFacility> departmentFacilities = dfDepartmentFacilityExtendRepository.findAll();

            for (DepartmentCampusResponse departmentCampusResponse : departmentCampusData) {
                Department department = departmentRepository.findDepartmentByDepartmentIdentityId(departmentCampusResponse.getDepartmentId()).orElse(null);
                Facility facility = facilityRepository.findMajorByFacilityIdentityId(departmentCampusResponse.getCampusId()).orElse(null);

                if (department == null || facility == null) {
                    return ResponseObject.successForward(null,"Vui lòng đồng bộ cơ sở và bộ môn.");
                }

                if (departmentFacilities.isEmpty()) {
                    syncDepartmentCampus(null, departmentCampusResponse, department, facility);
                } else {
                    for (DepartmentFacility departmentFacility : departmentFacilities) {
                        syncDepartmentCampus(departmentFacility, departmentCampusResponse, department, facility);
                    }
                }
            }
            return ResponseObject.successForward(null, "Đồng bộ môn và cơ sở thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.errorForward("Đồng bộ bộ môn và cơ sở không thành công! Đã xảy ra lỗi.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void syncDepartmentCampus(DepartmentFacility departmentFacility, DepartmentCampusResponse departmentCampusResponse, Department department, Facility facility) {
        DepartmentFacility postDepartmentFacility;

        if (departmentFacility == null) {
            postDepartmentFacility = new DepartmentFacility();
        } else {
            postDepartmentFacility = departmentFacilityRepository.findDepartmentFacilityByDepartmentFacility(departmentCampusResponse.getDepartmentCampusId())
                    .orElseGet(DepartmentFacility::new);
        }
        postDepartmentFacility.setDepartmentFacilityIdentityId(departmentCampusResponse.getDepartmentCampusId());
        postDepartmentFacility.setDepartment(department);
        postDepartmentFacility.setFacility(facility);
    }


}