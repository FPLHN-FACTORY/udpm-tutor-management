package udpm.hn.server.core.teacher.tutorclass.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCUpdateLectureRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCUpdateTutorClassDetailRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCTutorClassListRequest;
import java.util.List;

public interface TCTCTutorClassService {

    ResponseObject<?> getTutorClassesByTeacher(TCTCTutorClassListRequest request);

    ResponseObject<?> getLecturesByTutorClassDetailId(String id);

    ResponseObject<?> updateTutorClassDetail(List<TCTCUpdateTutorClassDetailRequest> request);

    ResponseObject<?> updateLecture(List<TCTCUpdateLectureRequest> list);

}
