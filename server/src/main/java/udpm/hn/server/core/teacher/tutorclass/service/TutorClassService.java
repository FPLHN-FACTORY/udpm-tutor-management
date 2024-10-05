package udpm.hn.server.core.teacher.tutorclass.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.teacher.tutorclass.model.request.TutorClassListRequest;

public interface TutorClassService {

    ResponseObject<?> getTutorClassesByTeacher(String teacherId, TutorClassListRequest request);

}
