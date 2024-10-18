package udpm.hn.server.core.teacher.student.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.teacher.student.model.request.StudentAttendanceRequest;
import udpm.hn.server.core.teacher.tutorclass.model.request.TCTCUpdateTutorClassDetailRequest;

import java.util.List;

public interface TCSStudentService {

    ResponseObject<?> getStudentByLectureId(String lectureId);

    ResponseObject<?> createAttendance(List<StudentAttendanceRequest> request);

}
