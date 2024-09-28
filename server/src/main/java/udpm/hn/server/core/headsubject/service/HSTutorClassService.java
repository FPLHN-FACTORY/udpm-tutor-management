package udpm.hn.server.core.headsubject.service;


import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.headsubject.model.request.CreateTutorClassRequest;
import udpm.hn.server.core.headsubject.model.request.SHTutorListRequest;
import udpm.hn.server.core.headsubject.model.request.UpdateNumberOfClassesRequest;

public interface HSTutorClassService {

    ResponseObject<?> createOrUpdateTutorClass(CreateTutorClassRequest request);
    ResponseObject<?> updateNumberOfClassesTutorClass(UpdateNumberOfClassesRequest request);

    ResponseObject<?> updateStatusApproveClassesTutorClass(String id);

    ResponseObject<?> deleteTutorClass(String id);

    ResponseObject<?> getDetailTutorClass(String id);

    ResponseObject<?> getTutorClasses(SHTutorListRequest request);

}
