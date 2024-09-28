package udpm.hn.server.core.headsubject.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headsubject.model.request.CreateTutorClassDetailRequest;
import udpm.hn.server.core.headsubject.model.request.TutorClassDetailRequest;

public interface HSTutorClassDetailService  {
    ResponseObject<?> createTutorClassDetail(CreateTutorClassDetailRequest request);
    ResponseObject<?> getTutorClassDetailByTutorClassId(TutorClassDetailRequest request);
}
