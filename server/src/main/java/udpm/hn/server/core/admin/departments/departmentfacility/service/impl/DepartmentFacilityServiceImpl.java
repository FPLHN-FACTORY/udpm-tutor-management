package udpm.hn.server.core.admin.departments.departmentfacility.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.CreateOrUpdateDepartmentFacilityRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.FindFacilityDetailRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.model.response.DepartmentFacilityResponse;
import udpm.hn.server.core.admin.departments.departmentfacility.repository.DFDepartmentExtendRepository;
import udpm.hn.server.core.admin.departments.departmentfacility.repository.DFDepartmentFacilityExtendRepository;
import udpm.hn.server.core.admin.departments.departmentfacility.repository.DFFacilityExtendRepository;
import udpm.hn.server.core.admin.departments.departmentfacility.repository.DFStaffExtendRepository;
import udpm.hn.server.core.admin.departments.departmentfacility.service.DepartmentFacilityService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.superadmin.operation.model.request.OperationLogsRequest;
import udpm.hn.server.core.superadmin.operation.service.OperationLogsService;
import udpm.hn.server.entity.Department;
import udpm.hn.server.entity.DepartmentFacility;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.entity.MajorFacility;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.connection.IdentityConnection;
import udpm.hn.server.infrastructure.connection.response.DepartmentCampusResponse;
import udpm.hn.server.infrastructure.connection.response.MajorCampusResponse;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.FunctionLogType;
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
@Slf4j
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

    private final OperationLogsService logsService;

    @Override
    public ResponseObject<?> getAllDepartmentFacility(String id, FindFacilityDetailRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.SEARCH);
        log.setStatus(true);
        try {
            Pageable pageable = Helper.createPageable(request, "createdDate");
            Page<DepartmentFacilityResponse> responses = dfDepartmentFacilityExtendRepository.getDepartmentFacilitiesByValueFind(id, pageable, request);
            log.setResponse(responses.getContent());
            return new ResponseObject<>(
                    PageableObject.of(responses),
                    HttpStatus.OK,
                    "Lấy thành công danh sách bộ môn theo cơ sở"
            );
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình lấy danh sách bộ môn theo cơ sở");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> addDepartmentFacility(@Valid CreateOrUpdateDepartmentFacilityRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.CREATE);
        log.setStatus(true);
        try {
            Optional<Department> departmentOptional = dfDepartmentExtendRepository.findById(request.getDepartmentId());
            Optional<Facility> facilityOptional = dfFacilityExtendRepository.findById(request.getFacilityId());
            Optional<Staff> staffOptional = dfStaffExtendRepository.findById(request.getHeadOfDepartmentId());

            if (dfDepartmentFacilityExtendRepository.existsByIdDepartmentAndIdFacilityAndIdAdd(request).isPresent()) {
                log.setStatus(false);
                log.setErrorMessage("Bộ môn theo cơ sở đã tồn tại!");
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
            }

            if (facilityOptional.isPresent() && staffOptional.isPresent() && departmentOptional.isPresent()) {
                DepartmentFacility addDepartmentFacility = new DepartmentFacility();
                addDepartmentFacility.setDepartment(departmentOptional.get());
                addDepartmentFacility.setFacility(facilityOptional.get());
                addDepartmentFacility.setStaff(staffOptional.get());
                addDepartmentFacility.setStatus(EntityStatus.ACTIVE);
                dfDepartmentFacilityExtendRepository.save(addDepartmentFacility);
                log.setSuccessMessage("Thêm thành công bộ môn");
                return new ResponseObject<>(null, HttpStatus.CREATED, log.getSuccessMessage());
            } else if (facilityOptional.isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("Không tìm thấy cơ sở trên");
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
            } else if (staffOptional.isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("Không tìm thấy CNBM trên");
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
            } else {
                log.setStatus(false);
                log.setErrorMessage("Không tìm thấy bộ môn trên");
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
            }
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình thêm bộ môn theo cơ sở");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> updateDepartmentFacility(@Valid CreateOrUpdateDepartmentFacilityRequest request, String id) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.UPDATE);
        log.setStatus(true);
        try {
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
                            log.setStatus(false);
                            log.setErrorMessage("Cập nhật trùng cơ sở hoặc để như cũ");
                            return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
                        }
                        updateDepartmentFacility.setStaff(staffOptional.get());
                        updateDepartmentFacility.setDepartment(departmentOptional.get());
                        updateDepartmentFacility.setFacility(facilityOptional.get());
                        dfDepartmentFacilityExtendRepository.save(updateDepartmentFacility);

                    }
                    log.setSuccessMessage("Sửa thành công !");
                    return new ResponseObject<>(null, HttpStatus.OK, log.getSuccessMessage());
                } else if (facilityOptional.isEmpty()) {
                    log.setStatus(false);
                    log.setErrorMessage("Không tìm thấy cơ sở trên");
                    return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
                } else if (staffOptional.isEmpty()) {
                    log.setStatus(false);
                    log.setErrorMessage("Không tìm thấy nhân viên trên");
                    return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
                } else if (departmentOptional.isEmpty()) {
                    log.setStatus(false);
                    log.setErrorMessage("Không tìm thấy bộ môn trên");
                    return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
                } else {
                    log.setStatus(false);
                    log.setErrorMessage("Sửa thất bại");
                    return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
                }
            } else {
                log.setStatus(false);
                log.setErrorMessage("Bộ môn theo cơ sở trên không tồn tại");
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, log.getErrorMessage());
            }
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình cập nhật bộ môn theo cơ sở");
        } finally {
            logsService.createOperationLog(log);
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
                Facility facility = facilityRepository.findFacilityByFacilityIdentityId(departmentCampusResponse.getCampusId()).orElse(null);

                if (department == null) {
                    return ResponseObject.errorForward("Vui lòng đồng bộ bộ môn", HttpStatus.INTERNAL_SERVER_ERROR);
                }

                if (facility == null) {
                    return ResponseObject.errorForward("Vui lòng đồng bộ cơ sở", HttpStatus.INTERNAL_SERVER_ERROR);
                }

                if (departmentFacilities.isEmpty()) {
                    syncDepartmentCampus(null, departmentCampusResponse, department, facility);
                } else {
                    DepartmentFacility correspondingFacility = departmentFacilityRepository.findDepartmentFacilityByDepartmentFacility(departmentCampusResponse.getDepartmentCampusId()).orElse(null);
                    syncDepartmentCampus(correspondingFacility, departmentCampusResponse, department, facility);
                }
            }

            return ResponseObject.successForward(null, "Đồng bộ môn theo cơ sở thành công!");
        } catch (RuntimeException e) {
            return ResponseObject.errorForward(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Lỗi khi đồng bộ bộ môn theo cơ sở : {}", e.getMessage());
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
        Optional<Staff> staff = staffRepository.findStaffByEmail(departmentCampusResponse.getEmailHeadDepartmentFpt(), departmentCampusResponse.getEmailHeadDepartmentFe());
        if (staff.isEmpty()) {
            throw new RuntimeException("Dữ liệu nhân viên chưa được đồng bộ, vui lòng đồng dữ liệu nhân viên");
        }
        postDepartmentFacility.setStaff(staff.get());
        postDepartmentFacility.setDepartmentFacilityIdentityId(departmentCampusResponse.getDepartmentCampusId());
        postDepartmentFacility.setDepartment(department);
        postDepartmentFacility.setFacility(facility);
        departmentFacilityRepository.save(postDepartmentFacility);
    }

}