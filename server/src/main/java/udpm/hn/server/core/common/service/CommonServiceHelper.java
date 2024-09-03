package udpm.hn.server.core.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.common.model.request.CMOptionsFilterRequest;
import udpm.hn.server.core.common.model.request.StaffSearchRequest;
import udpm.hn.server.core.common.model.response.SemesterInfoResponse;
import udpm.hn.server.core.common.repository.CMDepartmentExtendRepository;
import udpm.hn.server.core.common.repository.CMFacilityExtendRepository;
import udpm.hn.server.core.common.repository.CMSemesterExtendRepository;
import udpm.hn.server.core.common.repository.CMStaffExtendRepository;
import udpm.hn.server.utils.SessionHelper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommonServiceHelper {

    private final CMSemesterExtendRepository cMSemesterExtendRepository;

    private final CMStaffExtendRepository cMStaffExtendRepository;

    private final CMDepartmentExtendRepository cmDepartmentExtendRepository;

    private final CMFacilityExtendRepository facilityExtendRepository;

    private final SessionHelper sessionHelper;

    public ResponseObject<?> getSemesterInfo() {

        List<SemesterInfoResponse> semesterInfos = cMSemesterExtendRepository.getSemesterInfos();

        String currentSemester = sessionHelper.getCurrentSemesterId();

        if (currentSemester == null) {
            return new ResponseObject<>(
                    semesterInfos,
                    HttpStatus.OK,
                    "Lấy thông tin học kỳ thành công"
            );
        }

        semesterInfos.sort((o1, o2) -> {
            if (o1.getId().equals(currentSemester)) {
                return -1;
            } else if (o2.getId().equals(currentSemester)) {
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
        request.setCurrentDepartmentFacilityId(sessionHelper.getCurrentUserDepartmentFacilityId());
        return new ResponseObject<>(
                cMStaffExtendRepository.getStaffs(request),
                HttpStatus.OK,
                "Lấy thông tin nhân viên thành công"
        );
    }

    public ResponseObject<?> getAllDepartmentSubject(CMOptionsFilterRequest request) {
        return new ResponseObject<>(
                cmDepartmentExtendRepository.getAllDepartment(request),
                HttpStatus.OK,
                "Lấy danh sách bộ môn thành công"
        );
    }

    public ResponseObject<?> getAllFacility(CMOptionsFilterRequest request) {
        return new ResponseObject<>(
                facilityExtendRepository.getFacilities(request),
                HttpStatus.OK,
                "Lấy danh sách cơ sở thành công"
        );
    }

}
