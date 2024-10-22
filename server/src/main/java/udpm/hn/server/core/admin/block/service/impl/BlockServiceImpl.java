package udpm.hn.server.core.admin.block.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import udpm.hn.server.core.admin.block.model.request.CreateUpdateBlockRequest;
import udpm.hn.server.core.admin.block.model.response.BlockResponse;
import udpm.hn.server.core.admin.block.repository.BlockExtendRepository;
import udpm.hn.server.core.admin.block.service.BlockService;
import udpm.hn.server.core.admin.semester.repository.SemesterExtendRepository;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.superadmin.operation.model.request.OperationLogsRequest;
import udpm.hn.server.core.superadmin.operation.service.OperationLogsService;
import udpm.hn.server.entity.Block;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.infrastructure.constant.BlockName;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.FunctionLogType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class BlockServiceImpl implements BlockService {

    private final BlockExtendRepository blockExtendRepository;

    private final SemesterExtendRepository semesterExtendRepository;

    private final OperationLogsService logsService;

    @Override
    public ResponseObject<?> getAllBlockBySemesterId(String semesterId) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(semesterId);
        log.setTypeFunction(FunctionLogType.SEARCH);
        log.setStatus(true);
        try {
            List<BlockResponse> blockResponses = blockExtendRepository.getAllBlockBySemesterId(semesterId);
            log.setResponse(blockResponses);
            return new ResponseObject<>(blockResponses
                    ,
                    HttpStatus.OK,
                    "Lấy danh sách block thành công");
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình lấy danh sách block");
        } finally {
            logsService.createOperationLog(log);
        }

    }

    @Override
    public ResponseObject<?> createBlock(CreateUpdateBlockRequest createUpdateBlockRequest) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(createUpdateBlockRequest);
        log.setTypeFunction(FunctionLogType.CREATE);
        log.setStatus(true);
        try {
            Long startTime = createUpdateBlockRequest.getStartTime();
            Long endTime = createUpdateBlockRequest.getEndTime();

            Optional<Semester> existingSemester = semesterExtendRepository.findById(createUpdateBlockRequest.getSemesterId());
            if (existingSemester.isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("Học kỳ không tồn tại!");
                return new ResponseObject<>(null, HttpStatus.CONFLICT,
                        log.getErrorMessage());
            }
            Integer semesterYear = existingSemester.get().getYear();
            Optional<Block> existingBlock = blockExtendRepository
                    .findBlockByNameAndSemesterId(BlockName.valueOf(createUpdateBlockRequest.getBlockName()),
                            createUpdateBlockRequest.getSemesterId());

            if (existingBlock.isPresent()) {
                log.setStatus(false);
                log.setErrorMessage("Block đã tồn tại!");
                return new ResponseObject<>(null, HttpStatus.CONFLICT,
                        log.getErrorMessage());
            }

            if (startTime.compareTo(endTime) >= 0) {
                log.setStatus(false);
                log.setErrorMessage("Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc!");
                return new ResponseObject<>(null, HttpStatus.CONFLICT,
                        log.getErrorMessage());
            }

            LocalDate localDateStartTime = LocalDate.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault());
            LocalDate localDateEndTime = LocalDate.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault());
            Integer yearStartTime = localDateStartTime.getYear();
            Integer yearEndTime = localDateEndTime.getYear();
            if (yearStartTime.compareTo(semesterYear) != 0 && yearEndTime.compareTo(semesterYear) != 0) {
                log.setStatus(false);
                log.setErrorMessage("Thời gian bắt đầu và kết thúc phải trùng với năm học!");
                return new ResponseObject<>(null, HttpStatus.CONFLICT,
                        log.getErrorMessage());
            }

            Block block = new Block();
            block.setName(BlockName.valueOf(createUpdateBlockRequest.getBlockName()));
            block.setSemester(existingSemester.get());
            block.setStartTime(createUpdateBlockRequest.getStartTime());
            block.setEndTime(createUpdateBlockRequest.getEndTime());
            block.setStatus(EntityStatus.ACTIVE);
            blockExtendRepository.save(block);
            log.setSuccessMessage("Tạo block thành công!");
            return new ResponseObject<>(null, HttpStatus.CREATED, log.getSuccessMessage());
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình thêm block");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> getDetailBlock(String blockId) {
        return new ResponseObject<>(
                blockExtendRepository.getDetailBlockById(blockId),
                HttpStatus.OK,
                "Lấy thông tin block thành công!"
        );
    }

    @Override
    public ResponseObject<?> updateBlock(String blockId, CreateUpdateBlockRequest createUpdateBlockRequest) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        OperationLogsRequest log = new OperationLogsRequest();
        log.setHttpRequest(httpServletRequest);
        log.setRequest(createUpdateBlockRequest);
        log.setTypeFunction(FunctionLogType.UPDATE);
        log.setStatus(true);
        try {
            Long startTime = createUpdateBlockRequest.getStartTime();
            Long endTime = createUpdateBlockRequest.getEndTime();

            Optional<Block> existingBlock = blockExtendRepository.findById(blockId);
            if (existingBlock.isEmpty()) {
                log.setStatus(false);
                log.setErrorMessage("Block không tồn tại!");
                return new ResponseObject<>(null, HttpStatus.NOT_FOUND,
                        log.getErrorMessage());
            }

            Integer semesterYear = existingBlock.get().getSemester().getYear();
            String semesterId = createUpdateBlockRequest.getSemesterId();

            Optional<Block> existingByBlockAndSemester = blockExtendRepository
                    .existingByBlockAndSemester(createUpdateBlockRequest.getBlockName(), semesterId);
            if (existingByBlockAndSemester.isPresent() && !existingByBlockAndSemester.get().equals(existingBlock.get())) {
                log.setStatus(false);
                log.setErrorMessage("Block đã tồn tại!");
                return new ResponseObject<>(null, HttpStatus.CONFLICT,
                        log.getErrorMessage());
            }

            LocalDate localDateStartTime = LocalDate.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault());
            LocalDate localDateEndTime = LocalDate.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault());
            Integer yearStartTime = localDateStartTime.getYear();
            Integer yearEndTime = localDateEndTime.getYear();
            if (yearStartTime.compareTo(semesterYear) != 0 && yearEndTime.compareTo(semesterYear) != 0) {
                log.setStatus(false);
                log.setErrorMessage("Thời gian bắt đầu và kết thúc phải trùng với năm học!");
                return new ResponseObject<>(null, HttpStatus.CONFLICT,
                        log.getErrorMessage());
            }

            if (startTime.compareTo(endTime) >= 0) {
                log.setStatus(false);
                log.setErrorMessage("Thời gian bắt đầu phải trước thời gian kết thúc!");
                return new ResponseObject<>(null, HttpStatus.CONFLICT,
                        log.getErrorMessage());
            }

            Block block = new Block();
            block.setId(blockId);
            block.setName(BlockName.valueOf(createUpdateBlockRequest.getBlockName()));
            block.setSemester(existingBlock.get().getSemester());
            block.setStartTime(createUpdateBlockRequest.getStartTime());
            block.setEndTime(createUpdateBlockRequest.getEndTime());
            block.setStatus(existingBlock.get().getStatus());
            blockExtendRepository.save(block);
            log.setSuccessMessage("Cập nhật block thành công!");
            return new ResponseObject<>(null, HttpStatus.OK, log.getSuccessMessage());
        } catch (Exception e) {
            log.setStatus(false);
            log.setErrorMessage(e.getMessage());
            return new ResponseObject<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi trong quá trình cập nhật block");
        } finally {
            logsService.createOperationLog(log);
        }
    }

    @Override
    public ResponseObject<?> changeStatusBlock(String blockId) {
        Optional<Block> existingBlock = blockExtendRepository.findById(blockId);
        if (existingBlock.isPresent()) {
            Block block = existingBlock.get();
            if (block.getStatus().equals(EntityStatus.INACTIVE)) {
                block.setStatus(EntityStatus.ACTIVE);
            } else {
                block.setStatus(EntityStatus.INACTIVE);
            }
            blockExtendRepository.save(block);
            return new ResponseObject<>(null, HttpStatus.OK, "Thay đổi trạng thái block thành công!");
        }
        return new ResponseObject<>(null, HttpStatus.NOT_FOUND, "Block không tồn tại!");
    }

}