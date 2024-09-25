package udpm.hn.server.core.admin.departments.department.service.impl;

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
import udpm.hn.server.core.admin.departments.department.model.request.CreateOrUpdateMajorRequest;
import udpm.hn.server.core.admin.departments.department.model.request.FindMajorRequest;
import udpm.hn.server.core.admin.departments.department.repository.DepartmentExtendRepository;
import udpm.hn.server.core.admin.departments.department.repository.MajorExtendRepository;
import udpm.hn.server.core.admin.departments.department.service.MajorService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Department;
import udpm.hn.server.entity.Major;
import udpm.hn.server.infrastructure.connection.IdentityConnection;
import udpm.hn.server.infrastructure.connection.response.MajorResponse;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.repository.MajorRepository;
import udpm.hn.server.utils.Helper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {

    private final MajorExtendRepository majorExtendRepository;

    private final DepartmentExtendRepository departmentExtendRepository;

    private final IdentityConnection identityConnection;

    private final MajorRepository majorRepository;


    @Override
    public ResponseObject<?> getAllMajor(String id, FindMajorRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(majorExtendRepository.getAllMajorByDepartmentIdFilter(id, pageable, request)),
                HttpStatus.OK,
                "Lấy thành công danh sách chuyên ngành"
        );
    }

    @Override
    public ResponseObject<?> addMajor(@Valid CreateOrUpdateMajorRequest request) {
        request.setMajorName(request.getMajorName().replaceAll("\\s+", " "));
        Optional<Major> existsMajor = majorExtendRepository.findMajorByNameAndDepartmentId(request.getMajorName().trim(), request.getDepartmentId());
        Optional<Department> departmentOptional = departmentExtendRepository.findById(request.getDepartmentId());

        if (departmentOptional.isPresent()) {
            Department currentDepartment = departmentOptional.get();
            if (existsMajor.isEmpty()) {
                Major addMajor = new Major();
                addMajor.setName(request.getMajorName().trim());
                addMajor.setDepartment(currentDepartment);
                addMajor.setStatus(EntityStatus.ACTIVE);
                majorRepository.save(addMajor);

                return new ResponseObject<>(null, HttpStatus.CREATED, "Thêm chuyên ngành vào bộ môn " +
                        currentDepartment.getName() + " thành công");
            } else {
                return new ResponseObject<>(null, HttpStatus.CONFLICT, "Chuyên ngành đã tồn tại trong bộ môn " +
                        currentDepartment.getName());
            }
        } else {
            return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Bộ môn mà bạn đang thêm chuyên " +
                    "ngành không tồn tại [ " + departmentOptional.get().getName() + " ]");
        }
    }

    @Override
    public ResponseObject<?> updateMajor(@Valid CreateOrUpdateMajorRequest request, String id) {
        request.setMajorName(request.getMajorName().replaceAll("\\s+", " "));
        Optional<Major> majorOptional = majorRepository.findById(id);
        Optional<Department> departmentOptional = departmentExtendRepository.findById(request.getDepartmentId());

        if (departmentOptional.isPresent()) {
            Department currentDepartment = departmentOptional.get();
            if (majorOptional.isPresent()) {
                Major majorUpdate = majorOptional.get();

                if (!majorUpdate.getName().trim().equalsIgnoreCase(request.getMajorName().trim())) {
                    Optional<Major> existsMajor = majorExtendRepository.findMajorByNameAndDepartmentId(request.getMajorName().trim(), request.getDepartmentId());
                    if (existsMajor.isPresent()) {
                        return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Tên chuyên ngành đã tồn tại: " +
                                request.getMajorName().trim());
                    }
                }

                majorUpdate.setName(request.getMajorName().trim());
                majorUpdate.setDepartment(currentDepartment);
                majorRepository.save(majorUpdate);

                return new ResponseObject<>(null, HttpStatus.OK, "Cập nhât chuyên ngành vào bộ môn " +
                        currentDepartment.getName() + " thành công");
            } else {
                return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Chuyên ngành không tồn tại trong bộ môn " +
                        currentDepartment.getName());
            }
        } else {
            return new ResponseObject<>(null, HttpStatus.NOT_ACCEPTABLE, "Bộ môn mà bạn đang cập nhật chuyên ngành " +
                    "không tồn tại [ " + departmentOptional.get().getName() + " ]");
        }
    }

    @Override
    public ResponseObject<?> deleteMajor(String id) {
        Optional<Major> majorOptional = majorRepository.findById(id);

        if (majorOptional.isPresent()) {
            Major majorDelete = majorOptional.get();

            majorDelete.setStatus(
                    majorDelete.getStatus().name().equalsIgnoreCase(EntityStatus.ACTIVE.name())
                            ? EntityStatus.INACTIVE : EntityStatus.ACTIVE
            );
            majorRepository.save(majorDelete);
            return new ResponseObject<>(null, HttpStatus.OK, "Chuyển đổi thành công chuyên ngành " +
                    majorDelete.getName());
        } else {
            return new ResponseObject<>(null, HttpStatus.OK, "chuyên ngành không tồn tại trong bộ môn");
        }
    }

    @Override
    @Transactional
    public ResponseObject<?> synchronize() {
        try {
            List<MajorResponse> majorData = identityConnection.getMajors();
            List<Major> majors = majorRepository.findAll();

            if (majors.isEmpty()) {
                for (MajorResponse majorResponse : majorData) {
                    syncMajor(null, majorResponse);
                }
            } else {
                for (MajorResponse majorResponse : majorData) {
                    for (Major major : majors) {
                        syncMajor(major, majorResponse);
                    }
                }
            }
            return ResponseObject.successForward(null, "Đồng chuyên ngành thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseObject.errorForward("Đồng bộ chuyên ngành không thành công! Đã xảy ra lỗi.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void syncMajor(Major major, MajorResponse majorResponse) {
        Major postMajor;
        if (major == null) {
            postMajor = new Major();
        } else {
            Optional<Major> majorOptional = majorRepository.findMajorByMajorIdentityId(
                    majorResponse.getMajorId());
            postMajor = majorOptional.orElseGet(Major::new);
        }
        postMajor.setName(majorResponse.getMajorName());
        postMajor.setCode(majorResponse.getMajorCode());
        postMajor.setMajorIdentityId(majorResponse.getMajorId());
        postMajor.setDepartment(departmentExtendRepository.findDepartmentByDepartmentIdentityId(majorResponse.getDepartmentId()).orElse(null));
        majorRepository.save(postMajor);
    }

}