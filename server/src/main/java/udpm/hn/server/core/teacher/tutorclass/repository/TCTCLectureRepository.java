package udpm.hn.server.core.teacher.tutorclass.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.teacher.tutorclass.model.response.TCTCEvidenceLectureDetailResponse;
import udpm.hn.server.core.teacher.tutorclass.model.response.TCTCLectureResponse;
import udpm.hn.server.entity.Lecture;
import udpm.hn.server.entity.TutorClassDetail;
import udpm.hn.server.repository.LectureRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TCTCLectureRepository extends LectureRepository {

    List<Lecture> findByTutorClassDetail(TutorClassDetail tutorClassDetail);

    @Query(
            value =
                    """
                    SELECT
                        ROW_NUMBER() OVER(ORDER BY lt.created_date) AS orderNumber,
                        lt.id AS id,
                        lt.name AS name,
                        lt.start_date AS startTime,
                        lt.shift AS shift,
                        lt.record_link AS recordLink,
                        lt.lecture_content AS lectureContent,
                        lt.exercise_link AS exerciseLink,
                        lt.evidence_link AS evidenceLink,
                        lt.lecture_status AS lectureStatus,
                        lt.format AS format,
                        lt.evidence_link AS evidenceLink,
                        sub_ad.numberStudent AS numberStudents,
                        sub_csj.totalStudent AS totalStudents
                    FROM lecture lt
                    LEFT JOIN tutor_class_detail tcd on tcd.id = lt.tutor_class_detail_id
                    LEFT JOIN (
                        SELECT ad.lecture_id, COUNT(*) AS numberStudent
                        FROM attendance ad
                        GROUP BY ad.lecture_id
                    ) sub_ad ON sub_ad.lecture_id = lt.id
                    LEFT JOIN (
                        SELECT csj.tutor_class_detail_id, COUNT(*) AS totalStudent
                        FROM class_student_joined csj
                        GROUP BY csj.tutor_class_detail_id
                    ) sub_csj ON sub_csj.tutor_class_detail_id = tcd.id
                    WHERE lt.tutor_class_detail_id = :id
                    """,
            nativeQuery = true
    )
    List<TCTCLectureResponse> getLectures(String id);

    @Query("""
    select l.id as lectureId, l.evidenceLink as evidenceLink,
           l.exerciseLink as exerciseLink, l.recordLink as recordLink, l.driveLink as driveLink
    from Lecture l
    where l.id = :lectureId
      and (l.evidenceLink is not null or l.exerciseLink is not null 
           or l.recordLink is not null or l.driveLink is not null)
    """)
    TCTCEvidenceLectureDetailResponse getEvidenceLectureDetail(String lectureId);


    @Query("""
    select l.idDriveLink 
    from Lecture l 
    where l.id = :lectureId
    """)
    String getDriveLinkIdByLectureId(String lectureId);

}
