package udpm.hn.server.core.admin.facility.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.admin.facility.model.request.CreateUpdateFacilityRequest;
import udpm.hn.server.core.admin.facility.model.request.FacilityRequest;
import udpm.hn.server.core.admin.facility.repository.FacilityExtendRepository;
import udpm.hn.server.core.admin.facility.service.FacilityService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Block;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.infrastructure.connection.IdentityConnection;
import udpm.hn.server.infrastructure.connection.response.CampusResponse;
import udpm.hn.server.infrastructure.connection.response.SemesterResponse;
import udpm.hn.server.infrastructure.constant.BlockName;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.SemesterName;
import udpm.hn.server.utils.Helper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class FacilityServiceImpl implements FacilityService {

    private final FacilityExtendRepository facilityExtendRepository;

    private final IdentityConnection identityConnection;

    @Override
    public ResponseObject<?> getAllFacility(FacilityRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(facilityExtendRepository.getAllFacility(pageable, request)),
                HttpStatus.OK,
                "Get all facility successfully"
        );
    }

    @Override
    public ResponseObject<?> createFacility(CreateUpdateFacilityRequest request) {
        List<Facility> facilityOptional = facilityExtendRepository.findAllByName(request.getFacilityName().trim());
        if (!facilityOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cơ sở đã tồn tại");
        }
        String code = Helper.generateCodeFromName(request.getFacilityName());
        Facility facility = new Facility();
        facility.setCode(code);
        facility.setName(Helper.replaceManySpaceToOneSpace(request.getFacilityName()));
        facility.setCreatedDate(System.currentTimeMillis());
        facility.setStatus(EntityStatus.ACTIVE);
        facilityExtendRepository.save(facility);

        return new ResponseObject<>(null, HttpStatus.CREATED, "Thêm cơ sở thành công");
    }

    @Override
    public ResponseObject<?> updateFacility(String facilityId, CreateUpdateFacilityRequest request) {
        if (facilityExtendRepository.existsByNameAndIdNot(request.getFacilityName(), facilityId)) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Cơ sở đã tồn tại");
        }

        Optional<Facility> facilityOptional = facilityExtendRepository.findById(facilityId);


        facilityOptional.map(facility -> {
            facility.setCode(Helper.generateCodeFromName(request.getFacilityName().trim()));
            facility.setName(Helper.replaceManySpaceToOneSpace(request.getFacilityName().trim()));
            facility.setCreatedDate(facility.getCreatedDate());
            facility.setStatus(EntityStatus.ACTIVE);
            return facilityExtendRepository.save(facility);
        });

        return facilityOptional
                .map(subject -> new ResponseObject<>(null, HttpStatus.OK, "Cập nhật cơ sở thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Không tìm thấy cơ sở"));
    }

    @Override
    public ResponseObject<?> changeFacilityStatus(String facilityId) {
        Optional<Facility> facilityOptional = facilityExtendRepository.findById(facilityId);

        facilityOptional.map(facility -> {
            facility.setName(facility.getName());
            facility.setCreatedDate(facility.getCreatedDate());
            facility.setStatus(facility.getStatus() != EntityStatus.ACTIVE ? EntityStatus.ACTIVE : EntityStatus.INACTIVE);
            return facilityExtendRepository.save(facility);
        });

        return facilityOptional
                .map(subject -> new ResponseObject<>(null, HttpStatus.OK, "Đổi trạng thái cơ sở thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Không tìm thấy cơ sở"));
    }

    @Override
    public ResponseObject<?> getFacilityById(String facilityId) {
        return facilityExtendRepository.getDetailFacilityById(facilityId)
                .map(subject -> new ResponseObject<>(subject, HttpStatus.OK, "Get facility successfully"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Facility not found"));
    }

    @Override
    public ResponseObject<?> synchronize() {
        List<CampusResponse> responseData = identityConnection.getCampuses();
        List<Facility> facilities = facilityExtendRepository.findAll();

        if (facilities.isEmpty()) {
            // Nếu không có facility, đồng bộ tất cả các facility từ dữ liệu nhận được
            for (CampusResponse campusResponse : responseData) {
                syncFacility(null, campusResponse);
            }
        } else {
            // Nếu đã có facility, đồng bộ từng facility từ dữ liệu nhận được
            for (CampusResponse campusResponse : responseData) {
                for (Facility facility : facilities) {
                    syncFacility(facility, campusResponse);
                }
            }
        }
        return new ResponseObject<>(identityConnection.getCampuses(), HttpStatus.OK, "Lấy thành công");
    }

    private void syncFacility(Facility facility, CampusResponse campusResponse) {
        Facility postFacility;
        if (facility == null) {
            postFacility = new Facility();
            log.info("Create facility");
        } else {
            Optional<Facility> facilityOptional = facilityExtendRepository.findByCode(campusResponse.getCampusCode());
            postFacility = facilityOptional.orElseGet(Facility::new);
            log.info("Update facility");
        }

        postFacility.setName(campusResponse.getCampusName());
        postFacility.setFacilityIdentityId(campusResponse.getCampusId());
        postFacility.setCode(campusResponse.getCampusCode());
        postFacility.setStatus(EntityStatus.ACTIVE);
        postFacility.setFacilityIdentityId(campusResponse.getCampusId());

        // Lưu facility vào cơ sở dữ liệu
        facilityExtendRepository.save(postFacility);
    }

}
