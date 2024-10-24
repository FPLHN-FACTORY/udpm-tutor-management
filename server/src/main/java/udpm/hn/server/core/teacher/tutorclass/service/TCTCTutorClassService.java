package udpm.hn.server.core.teacher.tutorclass.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCCurrentPlanRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCEvidenceRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCTutorClassListRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCUpdateLectureRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCUpdateTutorClassDetailRequest;

import java.util.List;

public interface TCTCTutorClassService {

    ResponseObject<?> getTutorClassesByTeacher(TCTCTutorClassListRequest request);

    ResponseObject<?> getTutorClassDetail(String id);

    ResponseObject<?> getLecturesByTutorClassDetailId(String id);

    ResponseObject<?> updateTutorClassDetail(List<TCTCUpdateTutorClassDetailRequest> request);

    ResponseObject<?> updateLecture(List<TCTCUpdateLectureRequest> list);

    ResponseObject<?> evidenceLecture(TCTCEvidenceRequest request);

    ResponseObject<?> getEvidenceLectureDetail(String lectureId);

    ResponseObject<?> getPlan(TCTCCurrentPlanRequest request);

}
