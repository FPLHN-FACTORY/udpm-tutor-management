package udpm.hn.server.core.teacher.student.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.teacher.student.model.response.StudentAttendanceResponse;
import udpm.hn.server.repository.StudentRepository;

import java.util.List;

@Repository
public interface TCSStudentRepository extends StudentRepository {

    @Query(value = """
        SELECT
            ROW_NUMBER() OVER(ORDER BY a.created_date) AS orderNumber,
            tcd.room AS room,
            s.student_code AS studentCode,
            s.student_name AS studentName,
            s.student_email AS studentEmail,
            a.is_present AS isAttendance,
            a.reason AS attendanceReason,
            a.created_date AS attendanceTime,
            a.id as id,
            s.id as studentId,
            a.note as note
        FROM class_student_joined csj
        LEFT JOIN student s ON csj.student_id = s.id
        LEFT JOIN tutor_class_detail tcd ON csj.tutor_class_detail_id = tcd.id
        LEFT JOIN lecture l ON tcd.id = l.tutor_class_detail_id
        LEFT JOIN attendance a ON a.student_id = s.id AND a.lecture_id = l.id
        WHERE l.id = :lecturerId
    """, nativeQuery = true)
    List<StudentAttendanceResponse> getListStudentAttendance(@Param("lecturerId") String lecturerId);

}
