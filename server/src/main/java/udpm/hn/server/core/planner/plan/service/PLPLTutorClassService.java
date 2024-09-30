package udpm.hn.server.core.planner.plan.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.planner.plan.model.request.PLPLSubjectListRequest;
import udpm.hn.server.core.planner.plan.model.request.PLPLTutorClassDetailRequest;

public interface PLPLTutorClassService {

    ResponseObject<?> getDetailTutorClass(String id);
    ResponseObject<?> getTutorClasses(PLPLSubjectListRequest request);
    ResponseObject<?> getTutorClassDetailByTutorClassId(PLPLTutorClassDetailRequest request);

}
