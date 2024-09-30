package udpm.hn.server.core.headsubject.plan.service;


import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLCreateTutorClassRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLSubjectListRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLTutorClassDetailRequest;

public interface HSPLTutorClassService {

    ResponseObject<?> updateNumberOfClassesTutorClass(HSPLCreateTutorClassRequest request);
    ResponseObject<?> updateStatusApproveClassesTutorClass(String id);
    ResponseObject<?> getDetailTutorClass(String id);
    ResponseObject<?> getTutorClasses(HSPLSubjectListRequest request);
    ResponseObject<?> getTutorClassDetailByTutorClassId(HSPLTutorClassDetailRequest request);

}
