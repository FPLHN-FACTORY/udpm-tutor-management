package udpm.hn.server.core.admin.block.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import udpm.hn.server.core.admin.block.model.request.CreateUpdateBlockRequest;
import udpm.hn.server.core.admin.block.repository.BlockExtendRepository;
import udpm.hn.server.core.admin.block.service.BlockService;
import udpm.hn.server.core.admin.semester.repository.SemesterExtendRepository;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Block;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.infrastructure.constant.BlockName;
import udpm.hn.server.infrastructure.constant.EntityStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class BlockServiceImpl implements BlockService {

    private final BlockExtendRepository blockExtendRepository;

    private final SemesterExtendRepository semesterExtendRepository;

    @Override
    public ResponseObject<?> getAllBlockBySemesterId(String semesterId) {
        return new ResponseObject<>(
                blockExtendRepository.getAllBlockBySemesterId(semesterId),
                HttpStatus.OK,
                "Lấy danh sách block thành công");
    }

    @Override
    public ResponseObject<?> createBlock(CreateUpdateBlockRequest createUpdateBlockRequest) {
        Long startTime = createUpdateBlockRequest.getStartTime();
        Long endTime = createUpdateBlockRequest.getEndTime();

        Optional<Semester> existingSemester = semesterExtendRepository.findById(createUpdateBlockRequest.getSemesterId());
        if (existingSemester.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.CONFLICT,
                    "Học kỳ không tồn tại!");
        }
        Integer semesterYear = existingSemester.get().getYear();
        Optional<Block> existingBlock = blockExtendRepository
                .findBlockByNameAndSemesterId(BlockName.valueOf(createUpdateBlockRequest.getBlockName()),
                        createUpdateBlockRequest.getSemesterId());

        if (existingBlock.isPresent()) {
            return new ResponseObject<>(null, HttpStatus.CONFLICT,
                    "Block đã tồn tại!");
        }

        if (startTime.compareTo(endTime) >= 0) {
            return new ResponseObject<>(null, HttpStatus.CONFLICT,
                    "Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc!");
        }

        LocalDate localDateStartTime = LocalDate.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault());
        LocalDate localDateEndTime = LocalDate.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault());
        Integer yearStartTime = localDateStartTime.getYear();
        Integer yearEndTime = localDateEndTime.getYear();
        if (yearStartTime.compareTo(semesterYear) != 0 && yearEndTime.compareTo(semesterYear) != 0) {
            return new ResponseObject<>(null, HttpStatus.CONFLICT,
                    "Thời gian bắt đầu và kết thúc phải trùng với năm học!");
        }

        Block block = new Block();
        block.setName(BlockName.valueOf(createUpdateBlockRequest.getBlockName()));
        block.setSemester(existingSemester.get());
        block.setStartTime(createUpdateBlockRequest.getStartTime());
        block.setEndTime(createUpdateBlockRequest.getEndTime());
        block.setStatus(EntityStatus.ACTIVE);
        blockExtendRepository.save(block);
        return new ResponseObject<>(null, HttpStatus.CREATED, "Tạo block thành công!");
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
        Long startTime = createUpdateBlockRequest.getStartTime();
        Long endTime = createUpdateBlockRequest.getEndTime();

        Optional<Block> existingBlock = blockExtendRepository.findById(blockId);
        if (existingBlock.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.NOT_FOUND,
                    "Block không tồn tại!");
        }

        Integer semesterYear = existingBlock.get().getSemester().getYear();
        String semesterId = createUpdateBlockRequest.getSemesterId();

        Optional<Block> existingByBlockAndSemester = blockExtendRepository
                .existingByBlockAndSemester(createUpdateBlockRequest.getBlockName(), semesterId);
        if (existingByBlockAndSemester.isPresent() && !existingByBlockAndSemester.get().equals(existingBlock.get())) {
            return new ResponseObject<>(null, HttpStatus.CONFLICT,
                    "Block đã tồn tại!");
        }

        LocalDate localDateStartTime = LocalDate.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault());
        LocalDate localDateEndTime = LocalDate.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault());
        Integer yearStartTime = localDateStartTime.getYear();
        Integer yearEndTime = localDateEndTime.getYear();
        if (yearStartTime.compareTo(semesterYear) != 0 && yearEndTime.compareTo(semesterYear) != 0) {
            return new ResponseObject<>(null, HttpStatus.CONFLICT,
                    "Thời gian bắt đầu và kết thúc phải trùng với năm học!");
        }

        if (startTime.compareTo(endTime) >= 0) {
            return new ResponseObject<>(null, HttpStatus.CONFLICT,
                    "Thời gian bắt đầu phải trước thời gian kết thúc!");
        }

        Block block = new Block();
        block.setId(blockId);
        block.setName(BlockName.valueOf(createUpdateBlockRequest.getBlockName()));
        block.setSemester(existingBlock.get().getSemester());
        block.setStartTime(createUpdateBlockRequest.getStartTime());
        block.setEndTime(createUpdateBlockRequest.getEndTime());
        block.setStatus(existingBlock.get().getStatus());
        blockExtendRepository.save(block);
        return new ResponseObject<>(null, HttpStatus.OK, "Cập nhật block thành công!");
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