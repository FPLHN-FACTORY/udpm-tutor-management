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
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.CreateMajorFacilityRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.MajorFacilityRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.model.request.UpdateMajorFacilityRequest;
import udpm.hn.server.core.admin.departments.departmentfacility.model.response.FacilityDepartmentInfoResponse;
import udpm.hn.server.core.admin.departments.departmentfacility.model.response.MajorFacilitiesResponse;
import udpm.hn.server.core.admin.departments.departmentfacility.model.response.ModifyMajorFacilityResponse;
import udpm.hn.server.core.admin.departments.departmentfacility.repository.DFDepartmentFacilityExtendRepository;
import udpm.hn.server.core.admin.departments.departmentfacility.repository.DFMajorExtendRepository;
import udpm.hn.server.core.admin.departments.departmentfacility.repository.DFStaffExtendRepository;
import udpm.hn.server.core.admin.departments.departmentfacility.repository.MajorFacilityExtendRepository;
import udpm.hn.server.core.admin.departments.departmentfacility.service.MajorFacilityService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.DepartmentFacility;
import udpm.hn.server.entity.Major;
import udpm.hn.server.entity.MajorFacility;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.connection.IdentityConnection;
import udpm.hn.server.infrastructure.connection.response.MajorCampusResponse;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.utils.Helper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class MajorFacilityServiceImpl implements MajorFacilityService {

    private final MajorFacilityExtendRepository majorFacilityExtendRepository;

    private final DFDepartmentFacilityExtendRepository departmentFacilityRepository;

    private final DFStaffExtendRepository staffRepository;

    private final DFMajorExtendRepository majorRepository;

    private final IdentityConnection identityConnection;

    @Override

    public ResponseObject<?> getAllMajorFacilities(@Valid MajorFacilityRequest request) {
        Pageable pageable = Helper.createPageable(request, "id");

        try {
            PageableObject<?> listMajorFacility = PageableObject.of(majorFacilityExtendRepository.findAllMajorFacilities(request, pageable));
            FacilityDepartmentInfoResponse facilityDepartmentInfo = majorFacilityExtendRepository.getFacilityDepartmentInfo(request.getDepartmentFacilityId());
            MajorFacilitiesResponse majorFacilitiesResponse = new MajorFacilitiesResponse();
            majorFacilitiesResponse.setMajorFacilities(listMajorFacility);
            majorFacilitiesResponse.setFacilityDepartmentInfo(facilityDepartmentInfo);
            return ResponseObject.successForward(
                    majorFacilitiesResponse,
                    "Lấy danh sách chuyên ngành theo cơ sở thành công"
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing query", e);
        }
    }

    @Override
    public ResponseObject<?> getMajorFacilityById(String majorFacilityId) {
        return ResponseObject.successForward(
                majorFacilityExtendRepository.findMajorFacilityById(majorFacilityId),
                "Lấy thông tin chuyên ngành theo cơ sở thành công"
        );
    }

    @Override
    public ResponseObject<?> createMajorFacility(@Valid CreateMajorFacilityRequest request) {
        Optional<DepartmentFacility> departmentFacilityOptional = departmentFacilityRepository
                .findById(request.getDepartmentFacilityId());
        if (departmentFacilityOptional.isEmpty()) {
            return ResponseObject.errorForward(
                    "Không tìm thấy bộ môn theo cơ sở",
                    HttpStatus.BAD_REQUEST
            );
        }

        Optional<Staff> staffOptional = staffRepository.findById(request.getHeadMajorId());
        if (staffOptional.isEmpty()) {
            return ResponseObject.errorForward(
                    "Không tìm thấy nhân viên",
                    HttpStatus.BAD_REQUEST
            );
        }

        Optional<Major> majorOptional = majorRepository.findById(request.getMajorId());
        if (majorOptional.isEmpty()) {
            return ResponseObject.errorForward(
                    "Không tìm thấy chuyên ngành",
                    HttpStatus.BAD_REQUEST
            );
        }

        Optional<MajorFacility> majorFacilityOptional = majorFacilityExtendRepository
                .findByMajor_IdAndDepartmentFacility_Id(
                        request.getMajorId(),
                        request.getDepartmentFacilityId()
                );
        if (majorFacilityOptional.isPresent()) {
            return ResponseObject.errorForward(
                    "Chuyên ngành theo cơ sở đã tồn tại",
                    HttpStatus.BAD_REQUEST
            );
        }

        MajorFacility majorFacility = new MajorFacility();
        majorFacility.setMajor(majorOptional.get());
        majorFacility.setDepartmentFacility(departmentFacilityOptional.get());
        majorFacility.setStatus(EntityStatus.ACTIVE);
        majorFacility.setStaff(staffOptional.get());
        majorFacilityExtendRepository.save(majorFacility);

        return ResponseObject.successForward(
                new ModifyMajorFacilityResponse(
                        majorFacility.getMajor().getName(),
                        majorFacility.getDepartmentFacility().getFacility().getName(),
                        majorFacility.getDepartmentFacility().getDepartment().getName()
                ),
                "Tạo chuyên ngành theo cơ sở thành công"
        );
    }

    @Override
    public ResponseObject<?> updateMajorFacility(String majorFacilityId, @Valid UpdateMajorFacilityRequest request) {
        Optional<MajorFacility> majorFacilityOptional = majorFacilityExtendRepository.findById(majorFacilityId);
        if (majorFacilityOptional.isEmpty()) {
            return ResponseObject.errorForward(
                    "Không tìm thấy chuyên ngành theo cơ sở",
                    HttpStatus.NOT_FOUND
            );
        }

        Optional<Staff> staffOptional = staffRepository.findById(request.getHeadMajorId());
        if (staffOptional.isEmpty()) {
            return ResponseObject.errorForward(
                    "Không tìm thấy nhân viên",
                    HttpStatus.BAD_REQUEST
            );
        }

        Optional<Major> majorOptional = majorRepository.findById(request.getMajorId());
        if (majorOptional.isEmpty()) {
            return ResponseObject.errorForward(
                    "Không tìm thấy chuyên ngành",
                    HttpStatus.BAD_REQUEST
            );
        }

        MajorFacility majorFacility = majorFacilityOptional.get();
        majorFacility.setMajor(majorOptional.get());
        majorFacility.setStaff(staffOptional.get());
        majorFacilityExtendRepository.save(majorFacility);

        return ResponseObject.successForward(
                new ModifyMajorFacilityResponse(
                        majorFacility.getMajor().getName(),
                        majorFacility.getDepartmentFacility().getFacility().getName(),
                        majorFacility.getDepartmentFacility().getDepartment().getName()
                ),
                "Cập nhật chuyên ngành theo cơ sở thành công"
        );
    }

    @Override
    public ResponseObject<?> getAllMajors(String departmentId) {
        return ResponseObject.successForward(
                majorRepository.findAllByDepartmentId(departmentId),
                "Lấy danh sách chuyên ngành thành công"
        );
    }


    @Override
    @Transactional
    public ResponseObject<?> synchronize() {
        try {

            List<MajorCampusResponse> MajorCampusData = identityConnection.getMajorCampuses();
            List<MajorFacility> majorFacilities = majorFacilityExtendRepository.findAll();

            if (majorFacilities.isEmpty()) {
                for (MajorCampusResponse majorCampusResponse : MajorCampusData) {
                    syncMajorCampus(null, majorCampusResponse);
                }
            } else {
                for (MajorCampusResponse majorCampusResponse : MajorCampusData) {
                    for (MajorFacility majorFacility : majorFacilities) {
                        syncMajorCampus(majorFacility, majorCampusResponse);
                    }
                }
            }

            return ResponseObject.successForward(null, "Đồng chuyên ngành và cơ sở thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.errorForward("Đồng bộ chuyên ngành và cơ sở không thành công! Đã xảy ra lỗi.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private void syncMajorCampus(MajorFacility majorFacility, MajorCampusResponse majorCampusResponse) {

    }


}