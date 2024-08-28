package udpm.hn.server.core.admin.block.service;


import jakarta.validation.Valid;
import udpm.hn.server.core.admin.block.model.request.CreateUpdateBlockRequest;
import udpm.hn.server.core.common.base.ResponseObject;

public interface BlockService {

    ResponseObject<?> getAllBlockBySemesterId(String semesterId);

    ResponseObject<?> createBlock(@Valid CreateUpdateBlockRequest createUpdateBlockRequest);

    ResponseObject<?> getDetailBlock(String blockId);

    ResponseObject<?> updateBlock(String blockId, @Valid CreateUpdateBlockRequest createUpdateBlockRequest);

    ResponseObject<?> changeStatusBlock(String blockId);

}