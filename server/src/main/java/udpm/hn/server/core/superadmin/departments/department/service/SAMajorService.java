package udpm.hn.server.core.superadmin.departments.department.service;

import jakarta.validation.Valid;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.superadmin.departments.department.model.request.CreateOrUpdateMajorRequest;
import udpm.hn.server.core.superadmin.departments.department.model.request.FindMajorRequest;

public interface SAMajorService {

    ResponseObject<?> getAllMajor(String id, FindMajorRequest request);

    ResponseObject<?> addMajor(@Valid CreateOrUpdateMajorRequest request);

    ResponseObject<?> updateMajor(@Valid CreateOrUpdateMajorRequest request, String id);

    ResponseObject<?> deleteMajor(String id);

    ResponseObject<?> synchronize();

}
