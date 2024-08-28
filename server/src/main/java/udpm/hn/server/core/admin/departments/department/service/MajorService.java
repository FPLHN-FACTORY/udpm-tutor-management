package udpm.hn.server.core.admin.departments.department.service;

import jakarta.validation.Valid;
import udpm.hn.server.core.admin.departments.department.model.request.CreateOrUpdateMajorRequest;
import udpm.hn.server.core.admin.departments.department.model.request.FindMajorRequest;
import udpm.hn.server.core.common.base.ResponseObject;

public interface MajorService {

    ResponseObject<?> getAllMajor(String id, FindMajorRequest request);

    ResponseObject<?> addMajor(@Valid CreateOrUpdateMajorRequest request);

    ResponseObject<?> updateMajor(@Valid CreateOrUpdateMajorRequest request, String id);

    ResponseObject<?> deleteMajor(String id);
}
