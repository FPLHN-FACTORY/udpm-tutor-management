package udpm.hn.server.core.admin.block.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import udpm.hn.server.core.admin.block.model.request.CreateUpdateBlockRequest;
import udpm.hn.server.core.admin.block.service.BlockService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@Controller
@RequiredArgsConstructor
@RequestMapping(MappingConstants.REDIRECT_ADMIN_BLOCK)
public class BlockController {

    private final BlockService blockService;

    @GetMapping("/{semesterId}")
    public ResponseEntity<?> getAllBlockBySemesterId(@PathVariable String semesterId) {
        return Helper.createResponseEntity(blockService.getAllBlockBySemesterId(semesterId));
    }

    @PostMapping
    public ResponseEntity<?> createBlock(@RequestBody CreateUpdateBlockRequest createUpdateBlockRequest) {
        return Helper.createResponseEntity(blockService.createBlock(createUpdateBlockRequest));
    }

    @GetMapping("/get/{blockId}")
    public ResponseEntity<?> getDetailBlock(@PathVariable String blockId) {
        return Helper.createResponseEntity(blockService.getDetailBlock(blockId));
    }

    @PutMapping("/{blockId}")
    public ResponseEntity<?> updateBlock(@PathVariable String blockId, @RequestBody CreateUpdateBlockRequest createUpdateBlockRequest) {
        return Helper.createResponseEntity(blockService.updateBlock(blockId, createUpdateBlockRequest));
    }

    @PutMapping("/status/{blockId}")
    public ResponseEntity<?> changeStatusBlock(@PathVariable String blockId) {
        return Helper.createResponseEntity(blockService.changeStatusBlock(blockId));
    }

}