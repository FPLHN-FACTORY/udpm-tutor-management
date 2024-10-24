package udpm.hn.server.core.admin.semester.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import udpm.hn.server.core.admin.block.repository.BlockExtendRepository;
import udpm.hn.server.core.admin.semester.model.request.CreateUpdateSemesterRequest;
import udpm.hn.server.core.admin.semester.model.request.SemesterRequest;
import udpm.hn.server.core.admin.semester.repository.SemesterExtendRepository;
import udpm.hn.server.core.admin.semester.service.SemesterService;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.superadmin.operation.model.request.OperationLogsRequest;
import udpm.hn.server.core.superadmin.operation.service.OperationLogsService;
import udpm.hn.server.entity.Block;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.infrastructure.connection.IdentityConnection;
import udpm.hn.server.infrastructure.connection.response.SemesterResponse;
import udpm.hn.server.infrastructure.constant.BlockName;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.FunctionLogType;
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
public class SemesterServiceImpl implements SemesterService {

    private final SemesterExtendRepository semesterExtendRepository;

    private final BlockExtendRepository blockExtendRepository;

    private final IdentityConnection identityConnection;

    private final OperationLogsService logsService;

    @Override
    public ResponseObject<?> getAllSemester(SemesterRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.SEARCH);
        log.setStatus(true);
        try {
            Pageable pageable = Helper.createPageable(request, "createdDate");
            Page<udpm.hn.server.core.admin.semester.model.response.SemesterResponse> responses = semesterExtendRepository.getAllSemester(pageable, request);
            log.setResponse(responses.getContent());
            return new ResponseObject<>(
                    PageableObject.of(responses),
                    HttpStatus.OK,
                    "Lấy danh sách học kỳ thành công!"
            );
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình lấy danh học kỳ");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> getSemesterById(String semesterId) {
        return semesterExtendRepository.getDetailSemesterById(semesterId)
                .map(semester -> new ResponseObject<>(semester, HttpStatus.OK, "Lấy thông tin học kỳ thành công!"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Học kỳ không tồn tại!"));
    }

    @Override
    public ResponseObject<?> statusChangeSemester(String semesterId) {
        Optional<Semester> existingSemester = semesterExtendRepository.findById(semesterId);
        if (existingSemester.isPresent()) {
            Semester semester = existingSemester.get();
            List<Block> blockList = blockExtendRepository.findAllBySemesterId(semesterId);
            if (semester.getStatus().equals(EntityStatus.ACTIVE)) {
                semester.setStatus(EntityStatus.INACTIVE);
                for (Block block : blockList) {
                    block.setStatus(EntityStatus.INACTIVE);
                    blockExtendRepository.save(block);
                }
            } else {
                semester.setStatus(EntityStatus.ACTIVE);
            }
            semesterExtendRepository.save(semester);
            return new ResponseObject<>(null, HttpStatus.OK, "Thay đổi trạng thái học kỳ thành công!");

        }
        return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Học kỳ không tồn tại!");
    }

    @Override
    @Transactional
    public ResponseObject<?> synchronize() {
        try {
            List<SemesterResponse> semesterData = identityConnection.getSemesters();
            List<Semester> semesters = semesterExtendRepository.findAll();

            if (semesters.isEmpty()) {
                // Nếu không có semester, đồng bộ tất cả các semester từ dữ liệu nhận được
                for (SemesterResponse semesterResponse : semesterData) {
                    syncSemester(null, semesterResponse);
                }
            } else {
                for (SemesterResponse semesterResponse : semesterData) {
                    for (Semester semester : semesters) {
                        syncSemester(semester, semesterResponse);
                    }
                }
            }

            return ResponseObject.successForward(null, "Đồng bộ học kỳ và block thành công!");
        } catch (Exception e) {
            log.error("Lỗi khi đồng bộ học kỳ : {}", e.getMessage());
            return ResponseObject.errorForward("Đồng bộ học kỳ và block không thành công! Đã xảy ra lỗi.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void syncSemester(Semester semester, SemesterResponse semesterResponse) {
        // Khởi tạo hoặc lấy đối tượng Semester từ cơ sở dữ liệu
        Semester postSemester;
        if (semester == null) {
            postSemester = new Semester();
        } else {
            Optional<Semester> semesterOptional = semesterExtendRepository.findBySemesterId(semesterResponse.getId());
            postSemester = semesterOptional.orElseGet(Semester::new);
        }

        // Chuyển đổi thời gian và cập nhật thuộc tính của semester
        LocalDateTime startDate = Instant.ofEpochMilli(semesterResponse.getStartTime() * 1000)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        postSemester.setYear(startDate.getYear());
        postSemester.setSemesterName(SemesterName.valueOf(semesterResponse.getSemesterName()));
        postSemester.setStartTime(semesterResponse.getStartTime() * 1000);
        postSemester.setEndTime(semesterResponse.getEndTime() * 1000);
        postSemester.setSemesterId(semesterResponse.getId());
        postSemester.setStatus(EntityStatus.ACTIVE);
        // Lưu semester vào cơ sở dữ liệu
        semesterExtendRepository.save(postSemester);

        // Lấy danh sách blocks liên quan đến semester vừa lưu
        List<Block> blocks = blockExtendRepository.findBlockBySemesterId(postSemester.getId());

        if (blocks.isEmpty()) {
            // Nếu không có blocks, tạo mới hai block và lưu vào cơ sở dữ liệu
            Block block1 = new Block();
            Block block2 = new Block();

            block1.setName(BlockName.BLOCK_1);
            block1.setStartTime(semesterResponse.getStartTimeFirstBlock() * 1000);
            block1.setEndTime(semesterResponse.getEndTimeFirstBlock() * 1000);
            block1.setSemester(postSemester);
            block1.setStatus(EntityStatus.ACTIVE);

            block2.setName(BlockName.BLOCK_2);
            block2.setStartTime(semesterResponse.getStartTimeSecondBlock() * 1000);
            block2.setEndTime(semesterResponse.getEndTimeSecondBlock() * 1000);
            block2.setSemester(postSemester);
            block2.setStatus(EntityStatus.ACTIVE);

            blockExtendRepository.save(block1);
            blockExtendRepository.save(block2);
        } else {
            // Nếu có blocks, cập nhật thông tin của từng block
            for (Block block : blocks) {
                if (block.getName().equals(BlockName.BLOCK_1)) {
                    block.setStartTime(semesterResponse.getStartTimeFirstBlock() * 1000);
                    block.setEndTime(semesterResponse.getEndTimeFirstBlock() * 1000);
                } else {
                    block.setStartTime(semesterResponse.getStartTimeSecondBlock() * 1000);
                    block.setEndTime(semesterResponse.getEndTimeSecondBlock() * 1000);
                }
                block.setSemester(postSemester);
                blockExtendRepository.save(block);
            }
        }
    }

    @Override
    public ResponseObject<?> createSemester(CreateUpdateSemesterRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.CREATE);
        log.setStatus(true);
        try {
            LocalDateTime startDateSemester = Instant.ofEpochMilli(request.getStartTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            LocalDateTime endDateSemester = Instant.ofEpochMilli(request.getEndTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            Integer yearStartTime = startDateSemester.getYear();
            Integer yearEndTime = endDateSemester.getYear();
            String name = SemesterName.valueOf(request.getSemesterName()).toString();
            Long startTimeSemester = request.getStartTimeCustom();
            Long endTimeSemester = request.getEndTimeCustom();

            if (!yearStartTime.equals(yearEndTime)) {
                log.setStatus(false);
                log.setErrorMessage("Thời gian bắt đầu và kết thúc học kỳ phải cùng một năm!");
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, log.getErrorMessage());
            }

            if (request.getEndTimeBlock1() > endTimeSemester || request.getEndTimeBlock1() < startTimeSemester) {
                log.setStatus(false);
                log.setErrorMessage("Thời gian kết thúc block 1 phải nằm trong khoảng thời gian của học kỳ!");
                return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, log.getErrorMessage());
            }

            if (!semesterExtendRepository.checkConflictTime(startTimeSemester, endTimeSemester).isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("Đã có học kỳ trong khoảng thời gian này!");
                return new ResponseObject<>(null, HttpStatus.CONFLICT, log.getErrorMessage());
            }

            Semester semester = new Semester();
            semester.setSemesterName(SemesterName.valueOf(name));
            semester.setYear(yearStartTime);
            semester.setStartTime(startTimeSemester);
            semester.setEndTime(endTimeSemester);
            semester.setStatus(EntityStatus.ACTIVE);
            Semester semesterSave = semesterExtendRepository.save(semester);

            createBlock(semesterSave, request.getEndTimeBlock1Custom());
            log.setSuccessMessage("Thêm học kỳ, block thành công!");
            return new ResponseObject<>(null, HttpStatus.CREATED, log.getSuccessMessage());
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Thêm học kỳ thất bại!");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> updateSemester(String semesterId, CreateUpdateSemesterRequest request) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(request);
        log.setTypeFunction(FunctionLogType.UPDATE);
        log.setStatus(true);
        try {
            Optional<Semester> existingSemester = semesterExtendRepository.findById(semesterId);
            if (existingSemester.isPresent()) {
                Semester semester = existingSemester.get();
                LocalDateTime startDateSemester = Instant.ofEpochMilli(request.getStartTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                LocalDateTime endDateSemester = Instant.ofEpochMilli(request.getEndTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                Integer yearStartTime = startDateSemester.getYear();
                Integer yearEndTime = endDateSemester.getYear();
                Long startTimeSemester = request.getStartTimeCustom();
                Long endTimeSemester = request.getEndTimeCustom();

                if (!yearStartTime.equals(yearEndTime)) {
                    log.setStatus(false);
                    log.setErrorMessage("Thời gian bắt đầu và kết thúc học kỳ phải cùng một năm!");
                    return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, log.getErrorMessage());
                }

                if (request.getEndTimeBlock1() > endTimeSemester || request.getEndTimeBlock1() < startTimeSemester) {
                    log.setStatus(false);
                    log.setErrorMessage("Thời gian kết thúc block 1 phải nằm trong khoảng thời gian của học kỳ!");
                    return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, log.getErrorMessage());
                }

                List<Semester> semesters = semesterExtendRepository.checkConflictTime(startTimeSemester, endTimeSemester);
                if (!semesters.isEmpty()) {
                    for (Semester s : semesters) {
                        if (!s.getId().equals(semester.getId())) {
                            log.setStatus(false);
                            log.setErrorMessage("Đã có học kỳ trong khoảng thời gian này!");
                            return new ResponseObject<>(null, HttpStatus.CONFLICT, log.getErrorMessage());
                        }
                    }
                }

                String name = SemesterName.valueOf(request.getSemesterName()).toString();

//            Optional<Semester> existingByHocKyAndYear = semesterExtendRepository.existingBySemesterNameAndSemesterYear(name, yearStartTime);
//            if (existingByHocKyAndYear.isPresent() && !existingByHocKyAndYear.get().equals(semester)) {
//                return new ResponseObject<>(null, HttpStatus.CONFLICT,
//                        "Học kỳ đã tồn tại!");
//            }

                semester.setSemesterName(SemesterName.valueOf(request.getSemesterName()));
                semester.setYear(yearStartTime);
                semester.setStartTime(startTimeSemester);
                semester.setEndTime(endTimeSemester);
                Semester semesterSave = semesterExtendRepository.save(semester);

                createBlock(semesterSave, request.getEndTimeBlock1Custom());
                log.setSuccessMessage("Cập nhật học kỳ thành công!");
                return new ResponseObject<>(null, HttpStatus.OK, log.getSuccessMessage());
            }
            log.setStatus(false);
            log.setErrorMessage("Học kỳ không tồn tại!");
            return new ResponseObject<>(null, HttpStatus.NOT_FOUND, log.getErrorMessage());
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Có lỗi say ra khi cập nhật học kỳ!");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    public void createBlock(Semester semester, Long endTimeBlock1) {
        Block block1 = new Block();
        Optional<Block> block1Find = blockExtendRepository.findBlockByNameAndSemesterId(BlockName.BLOCK_1, semester.getId());
        if (block1Find.isPresent()) {
            block1 = block1Find.get();
        }
        block1.setSemester(semester);
        block1.setStatus(EntityStatus.ACTIVE);
        block1.setName(BlockName.BLOCK_1);
        block1.setStartTime(semester.getStartTime());
        LocalDateTime endDateBlock1 = Instant.ofEpochMilli(endTimeBlock1)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        block1.setEndTime(endTimeBlock1);
        blockExtendRepository.save(block1);

        Block block2 = new Block();
        Optional<Block> block2Find = blockExtendRepository.findBlockByNameAndSemesterId(BlockName.BLOCK_2, semester.getId());
        if (block2Find.isPresent()) {
            block2 = block2Find.get();
        }
        block2.setSemester(semester);
        block2.setName(BlockName.BLOCK_2);
        block2.setStatus(EntityStatus.ACTIVE);
        block2.setEndTime(semester.getEndTime());
        LocalDateTime nextDayStartTime = endDateBlock1.plusDays(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        block2.setStartTime(nextDayStartTime.atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli());
        blockExtendRepository.save(block2);
    }
}