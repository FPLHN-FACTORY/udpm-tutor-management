package udpm.hn.server.core.teacher.tutorclass.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCUpdateTutorClassDetailRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TutorClassListRequest;

import java.util.List;

public interface TutorClassService {

    ResponseObject<?> getTutorClassesByTeacher(String teacherId, TutorClassListRequest request);
    ResponseObject<?> updateTutorClassDetail(List<TCTCUpdateTutorClassDetailRequest> request);

}
