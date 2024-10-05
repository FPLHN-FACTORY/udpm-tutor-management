package udpm.hn.server.core.headsubject.plan.service;


import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLCreateTutorClassRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLSubjectListRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLTutorClassDetailRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLUpdateTutorClassDetailRequest;
import udpm.hn.server.core.headsubject.plan.model.request.HSPLUpdateTutorClassRequest;

public interface HSPLTutorClassService {

    ResponseObject<?> createTutorClass(HSPLCreateTutorClassRequest request);
    ResponseObject<?> updateTutorClass(String id, HSPLUpdateTutorClassRequest request);
    ResponseObject<?> getDetailTutorClass(String id);
    ResponseObject<?> getTutorClasses(HSPLSubjectListRequest request);
    ResponseObject<?> getTutorClassDetail(HSPLTutorClassDetailRequest request);
    ResponseObject<?> updateTutorClassDetail(String id, HSPLUpdateTutorClassDetailRequest request);
    ResponseObject<?> deleteTutorClassDetail(String id);
    ResponseObject<?> addTutorClassDetail(String id);

}
