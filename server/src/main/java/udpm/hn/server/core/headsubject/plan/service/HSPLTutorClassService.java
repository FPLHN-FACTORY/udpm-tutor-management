package udpm.hn.server.core.headsubject.plan.service;


import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headsubject.plan.model.request.CreateTutorClassRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLSubjectListRequest;
import udpm.hn.server.core.headsubject.plan.model.request.TutorClassDetailRequest;

public interface HSPLTutorClassService {

    ResponseObject<?> updateNumberOfClassesTutorClass(CreateTutorClassRequest request);
    ResponseObject<?> updateStatusApproveClassesTutorClass(String id);
    ResponseObject<?> getDetailTutorClass(String id);
    ResponseObject<?> getTutorClasses(HSPLSubjectListRequest request);
    ResponseObject<?> getTutorClassDetailByTutorClassId(TutorClassDetailRequest request);

}
