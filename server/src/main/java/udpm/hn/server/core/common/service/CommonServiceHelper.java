package udpm.hn.server.core.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.common.model.request.CMOptionsFilterRequest;
import udpm.hn.server.core.common.model.request.MajorSearchRequest;
import udpm.hn.server.core.common.model.request.RoleSearchRequest;
import udpm.hn.server.core.common.model.request.StaffSearchByRoleRequest;
import udpm.hn.server.core.common.model.request.StaffSearchRequest;
import udpm.hn.server.core.common.model.request.StudentSearchRequest;
import udpm.hn.server.core.common.model.response.SemesterInfoResponse;
import udpm.hn.server.core.common.repository.CMDepartmentExtendRepository;
import udpm.hn.server.core.common.repository.CMFacilityExtendRepository;
import udpm.hn.server.core.common.repository.CMMajorExtendRepository;
import udpm.hn.server.core.common.repository.CMRoleExtendRepository;
import udpm.hn.server.core.common.repository.CMSemesterExtendRepository;
import udpm.hn.server.core.common.repository.CMStaffExtendRepository;
import udpm.hn.server.core.common.repository.CMStudentTutorExtendRepository;
import udpm.hn.server.core.common.repository.CMSubjectExtendResponse;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommonServiceHelper {

    private final CMSemesterExtendRepository cMSemesterExtendRepository;

    private final CMStaffExtendRepository cMStaffExtendRepository;

    private final CMStudentTutorExtendRepository cmStudentTutorExtendRepository;

    private final CMDepartmentExtendRepository cmDepartmentExtendRepository;

    private final CMFacilityExtendRepository facilityExtendRepository;

    private final CMSubjectExtendResponse subjectExtendResponse;

    private final CMRoleExtendRepository roleExtendRepository;

    private final CMMajorExtendRepository majorExtendRepository;

    public ResponseObject<?> getSemesterInfo(String semesterId) {

        List<SemesterInfoResponse> semesterInfos = cMSemesterExtendRepository.getSemesterInfos();

        if (semesterId == null) {
            return new ResponseObject<>(
                    semesterInfos,
                    HttpStatus.OK,
                    "Lấy thông tin học kỳ thành công"
            );
        }

        semesterInfos.sort((o1, o2) -> {
            if (o1.getId().equals(semesterId)) {
                return -1;
            } else if (o2.getId().equals(semesterId)) {
                return 1;
            } else {
                return o2.getId().compareTo(o1.getId());
            }
        });

        return new ResponseObject<>(
                semesterInfos,
                HttpStatus.OK,
                "Lấy thông tin học kỳ thành công"
        );
    }

    public ResponseObject<?> getStaffSearch(StaffSearchRequest request) {
        return new ResponseObject<>(
                cMStaffExtendRepository.getStaffs(request),
                HttpStatus.OK,
                "Lấy thông tin nhân viên thành công"
        );
    }

    public ResponseObject<?> getStaffSearchByRole(StaffSearchByRoleRequest request) {
        request.setRole(Arrays.asList("GIANG_VIEN"));
        return new ResponseObject<>(
                cMStaffExtendRepository.getStaffsByRole(request),
                HttpStatus.OK,
                "Lấy thông tin nhân viên thành công"
        );
    }

    public ResponseObject<?> getSubject() {
        return new ResponseObject<>(
                subjectExtendResponse.getSubjectOptions(),
                HttpStatus.OK,
                "Lấy thông tin môn học thành công"
        );
    }

    public ResponseObject<?> getStudentTutor(StudentSearchRequest request) {
        return new ResponseObject<>(
                cmStudentTutorExtendRepository.getStudentTutor(request),
                HttpStatus.OK,
                "Lấy thông tin sinh viên thành công"
        );
    }

    public ResponseObject<?> getAllDepartmentSubject(CMOptionsFilterRequest request) {
        return new ResponseObject<>(
                cmDepartmentExtendRepository.getAllDepartment(request),
                HttpStatus.OK,
                "Lấy danh sách bộ môn thành công"
        );
    }

    public ResponseObject<?> getAllSemester(CMOptionsFilterRequest request) {
        return new ResponseObject<>(
                cMSemesterExtendRepository.getSemesterInfos(),
                HttpStatus.OK,
                "Lấy danh sách bộ môn thành công"
        );
    }

    public ResponseObject<?> getBlocks(String id) {
        return new ResponseObject<>(
                cMSemesterExtendRepository.getBlockInfos(id),
                HttpStatus.OK,
                "Lấy block thành công"
        );
    }

    public ResponseObject<?> getAllFacility(CMOptionsFilterRequest request) {
        return new ResponseObject<>(
                facilityExtendRepository.getFacilities(request),
                HttpStatus.OK,
                "Lấy danh sách cơ sở thành công"
        );
    }

    public ResponseObject<?> getRole(RoleSearchRequest request) {
        return new ResponseObject<>(
                roleExtendRepository.getRoles(request),
                HttpStatus.OK,
                "Lấy thông tin chức vụ thành công"
        );
    }

    public ResponseObject<?> getMajor(MajorSearchRequest request) {
        return new ResponseObject<>(
                majorExtendRepository.getAllMajorByDepartmentId(request),
                HttpStatus.OK,
                "Lấy thông tin chức vụ thành công"
        );
    }

    public ResponseObject<?> getStaffOptions(StaffSearchByRoleRequest request) {
        return new ResponseObject<>(
                cMStaffExtendRepository.getStaffOptions(request),
                HttpStatus.OK,
                "Lấy thông tin nhân viên thành công"
        );
    }
}
