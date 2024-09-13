package udpm.hn.server.core.admin.semester.service;

import jakarta.validation.Valid;
import udpm.hn.server.core.admin.semester.model.request.CreateUpdateSemesterRequest;
import udpm.hn.server.core.admin.semester.model.request.SemesterRequest;
import udpm.hn.server.core.common.base.ResponseObject;

public interface SemesterService {

    ResponseObject<?> getAllSemester(SemesterRequest request);

//    ResponseObject<?> createSemester(@Valid CreateUpdateSemesterRequest createUpdateSemesterRequest);
//
//    ResponseObject<?> updateSemester(String semesterId, @Valid CreateUpdateSemesterRequest createUpdateSemesterRequest);

    ResponseObject<?> getSemesterById(String semesterId);

    ResponseObject<?> statusChangeSemester(String semesterId);

}
