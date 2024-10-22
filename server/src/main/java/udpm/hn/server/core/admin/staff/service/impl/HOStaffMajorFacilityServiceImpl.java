package udpm.hn.server.core.admin.staff.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import udpm.hn.server.core.admin.staff.model.request.HOStaffMajorFacilityRequest;
import udpm.hn.server.core.admin.staff.model.response.HOStaffMajorFacilityDetailResponse;
import udpm.hn.server.core.admin.staff.repository.HOStaffDepartmentFacilityRepository;
import udpm.hn.server.core.admin.staff.repository.HOStaffMajorFacilityRepository;
import udpm.hn.server.core.admin.staff.repository.HOStaffMajorFacilityStaffRepository;
import udpm.hn.server.core.admin.staff.repository.HOStaffRepository;
import udpm.hn.server.core.admin.staff.service.HOStaffMajorFacilityService;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.superadmin.operation.model.request.OperationLogsRequest;
import udpm.hn.server.core.superadmin.operation.service.OperationLogsService;
import udpm.hn.server.entity.DepartmentFacility;
import udpm.hn.server.entity.MajorFacility;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.StaffMajorFacility;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.FunctionLogType;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class HOStaffMajorFacilityServiceImpl implements HOStaffMajorFacilityService {

    private final HOStaffMajorFacilityRepository majorFacilityRepository;

    private final HOStaffDepartmentFacilityRepository departmentFacilityRepository;

    private final HOStaffMajorFacilityStaffRepository staffMajorFacilityStaffRepository;

    private final HOStaffRepository staffRepository;

    private final OperationLogsService logsService;

    @Override
    public ResponseObject<?> getStaffMajorFacilities(String staffId) {
        return new ResponseObject<>(majorFacilityRepository.getMajorFacilities(staffId),
                HttpStatus.OK,
                "Lấy danh sách bộ môn chuyên ngành thành công");
    }

    @Override
    public ResponseObject<?> getDepartmentByFacility(String idFacility) {
        return new ResponseObject<>(majorFacilityRepository.getDepartmentByFacility(idFacility),
                HttpStatus.OK,
                "Lấy danh sách bộ môn theo cơ sở thành công");
    }

    @Override
    public ResponseObject<?> getMajorByDepartmentFacility(String idDepartment, String idFacility) {
        return new ResponseObject<>(majorFacilityRepository.getMajorByDepartmentFacility(idDepartment, idFacility),
                HttpStatus.OK,
                "Lấy chuyên ngành theo bộ môn theo cơ sở thành công");
    }

    @Override
    public ResponseObject<?> createStaffMajorFacility(@Valid HOStaffMajorFacilityRequest req) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(req);
        log.setTypeFunction(FunctionLogType.CREATE);
        log.setStatus(true);
        try {
            List<DepartmentFacility> departmentFacility =
                    departmentFacilityRepository.findAllByDepartment_IdAndFacility_Id(
                            req.getIdDepartment(),
                            req.getIdFacility()
                    );

            if (departmentFacility.isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("Bộ môn theo cơ sở không tồn tại");
                return new ResponseObject<>(null,
                        HttpStatus.BAD_REQUEST,
                        log.getErrorMessage());
            }

            List<MajorFacility> majorFacility =
                    majorFacilityRepository.findAllByDepartmentFacility_IdAndMajor_IdAndStatus(departmentFacility.get(0).getId(),
                            req.getIdMajor(),
                            EntityStatus.ACTIVE);

            if (majorFacility.isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("Chuyên ngành theo cơ sở không tồn tại");
                return new ResponseObject<>(null,
                        HttpStatus.BAD_REQUEST,
                        log.getErrorMessage());
            }

            Optional<Staff> staff = staffRepository.findByIdAndStatus(req.getIdStaff(), EntityStatus.ACTIVE);

            if (staff.isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("Nhân viên không tồn tại");
                return new ResponseObject<>(null,
                        HttpStatus.BAD_REQUEST,
                        log.getErrorMessage());
            }

            List<StaffMajorFacility> staffMajorFacilities = staffMajorFacilityStaffRepository.checkStaffMajorFacilityExists(req.getIdFacility(), req.getIdStaff());

            if (!staffMajorFacilities.isEmpty() && staffMajorFacilities.get(0).getStatus() == EntityStatus.ACTIVE) {
                log.setStatus(false);
                log.setErrorMessage("Một cơ sở chỉ được một bộ môn và chuyên ngành");
                return new ResponseObject<>(null,
                        HttpStatus.BAD_REQUEST,
                        log.getErrorMessage());
            }

            StaffMajorFacility staffMajorFacility = new StaffMajorFacility();
            staffMajorFacility.setMajorFacility(majorFacility.get(0));
            staffMajorFacility.setStaff(staff.get());
            staffMajorFacility.setStatus(EntityStatus.ACTIVE);
            staffMajorFacilityStaffRepository.save(staffMajorFacility);
            log.setSuccessMessage("Thêm bộ môn chuyên ngành theo cơ sở thành công");
            return new ResponseObject<>(null,
                    HttpStatus.OK,
                    log.getSuccessMessage());
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Có lỗi sảy ra khi thêm bộ môn chuyên ngành theo cơ sở cho nhân viên!");
        } finally {
            logsService.createOperationLog(log);
        }

    }

    @Override
    public ResponseObject<?> updateStaffMajorFacility(@Valid HOStaffMajorFacilityRequest req) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(req);
        log.setTypeFunction(FunctionLogType.UPDATE);
        log.setStatus(true);
        try {
            Optional<StaffMajorFacility> staffMajorFacility = staffMajorFacilityStaffRepository.findById(req.getIdStaffMajorFacility());

            if (staffMajorFacility.isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("nhân viên chuyên ngành theo cơ sở không tồn tại");
                return new ResponseObject<>(null,
                        HttpStatus.BAD_REQUEST,
                        log.getErrorMessage());
            }

            List<DepartmentFacility> departmentFacility =
                    departmentFacilityRepository.findAllByDepartment_IdAndFacility_Id(
                            req.getIdDepartment(),
                            req.getIdFacility()
                    );

            if (departmentFacility.isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("Bộ môn theo cơ sở không tồn tại");
                return new ResponseObject<>(null,
                        HttpStatus.BAD_REQUEST,
                        log.getErrorMessage());
            }

            List<MajorFacility> majorFacility =
                    majorFacilityRepository.findAllByDepartmentFacility_IdAndMajor_IdAndStatus(departmentFacility.get(0).getId(),
                            req.getIdMajor(),
                            EntityStatus.ACTIVE);

            if (majorFacility.isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("Chuyên ngành theo cơ sở không tồn tại");
                return new ResponseObject<>(null,
                        HttpStatus.BAD_REQUEST,
                        log.getErrorMessage());
            }

            staffMajorFacility.get().setMajorFacility(majorFacility.get(0));
            staffMajorFacility.get().setStatus(EntityStatus.ACTIVE);
            staffMajorFacilityStaffRepository.save(staffMajorFacility.get());
            log.setSuccessMessage("Sửa bộ môn chuyên ngành theo cơ sở thành công");
            return new ResponseObject<>(null,
                    HttpStatus.OK,
                    log.getSuccessMessage());
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Có lỗi sảy ra khi cập nhật bộ môn chuyên ngành theo cơ sở cho nhân viên!");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> deleteStaffMajorFacility(String idStaffMajorFacility) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(idStaffMajorFacility);
        log.setTypeFunction(FunctionLogType.DELETE);
        log.setStatus(true);
        try {
            Optional<StaffMajorFacility> staffMajorFacility = staffMajorFacilityStaffRepository.findById(idStaffMajorFacility);

            if (staffMajorFacility.isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("Chuyên ngành theo cơ sở không tồn tại");
                return new ResponseObject<>(null,
                        HttpStatus.BAD_REQUEST,
                        log.getErrorMessage());
            }
            log.setRequest(staffMajorFacility);
            staffMajorFacilityStaffRepository.deleteById(idStaffMajorFacility);
            log.setSuccessMessage("Xoá thành công.");
            return new ResponseObject<>(null,
                    HttpStatus.OK,
                    log.getSuccessMessage());
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Có lỗi sảy ra khi xóa bộ môn chuyên ngành theo cơ sở cho nhân viên!");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> detailStaffMajorFacility(String idStaffMajorFacility) {
        List<HOStaffMajorFacilityDetailResponse> detailResponses = staffMajorFacilityStaffRepository.detailStaffMajorFacility(idStaffMajorFacility);
        if (detailResponses.isEmpty()) {
            new ResponseObject<>(null,
                    HttpStatus.BAD_REQUEST,
                    "Chuyên ngành bộ môn của nhân viên này không tồn tại");
        }
        return new ResponseObject<>(detailResponses.get(0), HttpStatus.OK, "Lấy chuyên ngành bộ môn thành công");
    }
}
