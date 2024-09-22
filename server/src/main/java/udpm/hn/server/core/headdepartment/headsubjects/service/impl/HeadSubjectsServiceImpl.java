package udpm.hn.server.core.headdepartment.headsubjects.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.AssignOrUnAssignSubjectForHeadSubjectRequest;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.HeadSubjectRequest;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.HeadSubjectSearchRequest;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.ReassignHeadSubjectRequest;
import udpm.hn.server.core.headdepartment.headsubjects.model.request.SubjectByHeadSubjectRequest;
import udpm.hn.server.core.headdepartment.headsubjects.repository.*;
import udpm.hn.server.core.headdepartment.headsubjects.service.HeadSubjectsService;
import udpm.hn.server.entity.*;
import udpm.hn.server.infrastructure.constant.Role;
import udpm.hn.server.utils.Helper;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class HeadSubjectsServiceImpl implements HeadSubjectsService {

    private final HDHSHeadSubjectBySemesterRepository hdhsHeadSubjectBySemesterRepository;

    private final HDHSSubjectRepository hdhsSubjectRepository;

    private final HDHSStaffExtendRepository hdhsStaffExtendRepository;

    private final HDHSSFacilityExtendRepository hdhsFacilityExtendRepository;

    private final HDHSSSemesterExtendRepository hdhsSemesterExtendRepository;

    private final HDHSSDepartmentExtendRepository hdhsDepartmentExtendRepository;

    private final HDHSSDepartmentFacilityExtendRepository hdhssDepartmentFacilityExtendRepository;

    @Override
    public ResponseObject<?> getAllHeadSubjects(HeadSubjectRequest request) {
        request.setHeadSubjectRoleCode(Role.TRUONG_MON.name());
        return new ResponseObject<>(
                PageableObject.of(
                        hdhsHeadSubjectBySemesterRepository.getAllHeadSubjectsBySemester(
                                Helper.createPageable(request),
                                request
                        )
                ),
                HttpStatus.OK,
                "Lấy danh sách trưởng bộ môn thành công"
        );
    }

    @Override
    public ResponseObject<?> getSubjectsByHeadSubject(String headSubjectId, SubjectByHeadSubjectRequest request) {
        request.setHeadSubjectId(headSubjectId);
        return new ResponseObject<>(
                PageableObject.of(
                        hdhsSubjectRepository.getSubjectByHeadSubject(
                                Helper.createPageable(request),
                                request
                        )
                ),
                HttpStatus.OK,
                "Lấy danh sách môn học theo trưởng bộ môn thành công"
        );
    }

    @Override
    public ResponseObject<?> getSubjectsWithAssign(String headSubjectId, SubjectByHeadSubjectRequest request) {
        request.setHeadSubjectId(headSubjectId);
        return new ResponseObject<>(
                PageableObject.of(
                        hdhsSubjectRepository.getSubjectAssign(
                                Helper.createPageable(request, "orderNumber"),
                                request
                        )
                ),
                HttpStatus.OK,
                "Lấy danh sách môn học theo trưởng bộ môn thành công"
        );
    }

    @Override
    public ResponseObject<?> assignSubjectForHeadSubject(String headSubjectId, @Valid AssignOrUnAssignSubjectForHeadSubjectRequest request) {

        Optional<Staff> staff = hdhsStaffExtendRepository.findById(headSubjectId);

        if (staff.isEmpty()) {
            return ResponseObject.errorForward(
                    "Nhân viên không phải tồn tại",
                    HttpStatus.NOT_FOUND
            );
        }

        Optional<Subject> subject = hdhsSubjectRepository.findById(request.getSubjectId());

        if (subject.isEmpty()) {
            return ResponseObject.errorForward(
                    "Môn học không tồn tại",
                    HttpStatus.NOT_FOUND
            );
        }

        Optional<HeadSubjectBySemester> headSubjectBySemester = hdhsHeadSubjectBySemesterRepository
                .findBySemester_IdAndSubject_IdAndFacility_Id(
                        request.getSemesterId(),
                        request.getSubjectId(),
                        request.getFacilityId()
                );
        if (headSubjectBySemester.isPresent()) {
            headSubjectBySemester.get().setStaff(staff.get());
            hdhsHeadSubjectBySemesterRepository.save(headSubjectBySemester.get());
        } else {
            HeadSubjectBySemester headSubject = new HeadSubjectBySemester();
            headSubject.setSemester(hdhsSemesterExtendRepository.getReferenceById(request.getSemesterId()));
            headSubject.setSubject(subject.get());
            headSubject.setStaff(staff.get());
            headSubject.setFacility(hdhsFacilityExtendRepository.getReferenceById(request.getFacilityId()));
            hdhsHeadSubjectBySemesterRepository.save(headSubject);
        }
        return ResponseObject.successForward(
                HttpStatus.OK,
                "Gán môn học cho trưởng bộ môn thành công"
        );
    }

    @Override
    @Transactional
    public ResponseObject<?> unassignSubjectForHeadSubject(String headSubjectId, AssignOrUnAssignSubjectForHeadSubjectRequest request) {

        Optional<Staff> staff = hdhsStaffExtendRepository.findById(headSubjectId);
        if (staff.isEmpty()) {
            return ResponseObject.errorForward(
                    "Nhân viên không phải tồn tại",
                    HttpStatus.NOT_FOUND
            );
        }

        Optional<Subject> subject = hdhsSubjectRepository.findById(request.getSubjectId());
        if (subject.isEmpty()) {
            return ResponseObject.errorForward(
                    "Môn học không tồn tại",
                    HttpStatus.NOT_FOUND
            );
        }

        Optional<HeadSubjectBySemester> headSubjectBySemester = hdhsHeadSubjectBySemesterRepository
                .findBySemester_IdAndSubject_IdAndFacility_Id(
                        request.getSemesterId(),
                        request.getSubjectId(),
                        request.getFacilityId()
                );
        if (headSubjectBySemester.isPresent()) {
            hdhsHeadSubjectBySemesterRepository.delete(headSubjectBySemester.get());
            return ResponseObject.successForward(
                    HttpStatus.OK,
                    "Hủy gán môn học cho trưởng bộ môn thành công"
            );
        } else {
            return ResponseObject.errorForward(
                    "Môn học không được gán cho trưởng bộ môn",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Override
    public ResponseObject<?> reassignSubjectForAnotherHeadSubject(@Valid ReassignHeadSubjectRequest request) {

        String currentHeadSubjectId = request.getCurrentHeadSubjectId();
        String newHeadSubjectId = request.getNewHeadSubjectId();
        Optional<Staff> newHeadSubject = hdhsStaffExtendRepository.findById(newHeadSubjectId);
        if (newHeadSubject.isEmpty()) {
            return ResponseObject.errorForward(
                    "Nhân viên không phải tồn tại",
                    HttpStatus.NOT_FOUND
            );
        }

        List<HeadSubjectBySemester> headSubjectBySemesterOlds = hdhsHeadSubjectBySemesterRepository
                .findBySemester_IdAndFacility_IdAndStaff_Id(
                        request.getSemesterId(),
                        request.getFacilityId(),
                        currentHeadSubjectId
                );

        for (HeadSubjectBySemester headSubjectBySemester : headSubjectBySemesterOlds) {
            if (headSubjectBySemester.getStaff().getId().equals(newHeadSubjectId)) {
                return ResponseObject.errorForward(
                        "Nhân viên đã là trưởng bộ môn của môn học này",
                        HttpStatus.BAD_REQUEST
                );
            }
        }

        headSubjectBySemesterOlds.forEach(
                headSubjectBySemester ->
                        headSubjectBySemester.setStaff(newHeadSubject.get())
        );
        hdhsHeadSubjectBySemesterRepository.saveAll(headSubjectBySemesterOlds);

        return ResponseObject.successForward(
                HttpStatus.OK,
                "Chuyển môn học cho trưởng bộ môn khác thành công"
        );
    }

    @Override
    public ResponseObject<?> searchStaff(HeadSubjectSearchRequest request) {
        return new ResponseObject<>(
                hdhsHeadSubjectBySemesterRepository.getHeadSubjects(request, "TRUONG_MON"),
                HttpStatus.OK,
                "Tìm kiếm nhân viên thành công"
        );
    }

    @Override
    public ResponseObject<?> syncHeadSubjectAttachWithSubjectFromPreviousSemesterToCurrentSemester(String semesterId) {
        String previousSemesterId = getPreviousSemesterId(semesterId);
        if (previousSemesterId == null) {
            return ResponseObject.errorForward(
                    "Không tìm thấy học kỳ trước",
                    HttpStatus.NOT_FOUND
            );
        }

        Semester semester = hdhsSemesterExtendRepository.findById(previousSemesterId).orElse(null);
        List<HeadSubjectBySemester> headSubjectBySemesters = hdhsHeadSubjectBySemesterRepository
                .findBySemester(semester);
        if (headSubjectBySemesters.isEmpty()) {
            return ResponseObject.errorForward(
                    "Chưa phân công trưởng môn cho học kỳ trước",
                    HttpStatus.NOT_FOUND
            );
        }

        for (HeadSubjectBySemester headSubjectBySemester : headSubjectBySemesters) {
            Optional<HeadSubjectBySemester> headSubjectBySemesterOptional = hdhsHeadSubjectBySemesterRepository
                    .findBySemester_IdAndSubject_IdAndFacility_Id(
                            semesterId,
                            headSubjectBySemester.getSubject().getId(),
                            headSubjectBySemester.getFacility().getId()
                    );
            if (headSubjectBySemesterOptional.isEmpty()) {
                HeadSubjectBySemester newHeadSubjectBySemester = new HeadSubjectBySemester();
                newHeadSubjectBySemester.setSemester(hdhsSemesterExtendRepository.getReferenceById(semesterId));
                newHeadSubjectBySemester.setSubject(headSubjectBySemester.getSubject());
                newHeadSubjectBySemester.setStaff(headSubjectBySemester.getStaff());
                newHeadSubjectBySemester.setFacility(headSubjectBySemester.getFacility());
                hdhsHeadSubjectBySemesterRepository.save(newHeadSubjectBySemester);
            }
        }
        return ResponseObject.successForward(
                HttpStatus.OK,
                "Đồng bộ trưởng bộ môn từ học kỳ trước sang học kỳ hiện tại thành công"
        );
    }

    @Override
    public ResponseObject<?> checkCurrentSemesterHasHeadSubject(String semesterId) {
        Semester semester = hdhsSemesterExtendRepository.findById(semesterId).orElse(null);
        List<HeadSubjectBySemester> headSubjectBySemesters = hdhsHeadSubjectBySemesterRepository
                .findBySemester(semester);
        if (headSubjectBySemesters.isEmpty()) {
            return new ResponseObject<>(
                    true,
                    HttpStatus.OK,
                    "Học kỳ hiện tại chưa phân công trưởng bộ môn"
            );
        }
        return new ResponseObject<>(
                false,
                HttpStatus.OK,
                "Học kỳ hiện tại đã phân công trưởng bộ môn"
        );
    }

    private String getPreviousSemesterId(String semesterId) {
        Semester currentSemester = hdhsSemesterExtendRepository.getReferenceById(Objects.requireNonNull(semesterId));
        List<Semester> semesters = hdhsSemesterExtendRepository.findAll();

        return semesters.stream()
                .filter(semester -> semester.getStartTime() < currentSemester.getStartTime()
                        && !semester.getId().equals(currentSemester.getId()))
                .sorted(Comparator.comparing(Semester::getStartTime).reversed())
                .findFirst()
                .map(Semester::getId)
                .orElse(null);
    }


}
