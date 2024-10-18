package udpm.hn.server.core.teacher.student.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Attendance;
import udpm.hn.server.repository.AttendanceRepository;

import java.util.Optional;

@Repository
public interface TCSAttendanceRepository extends AttendanceRepository {

    @Query("""
        select a from Attendance a where a.student.id = :studentId and a.lecture.id = :lectureId
    """)
    Optional<Attendance> findAttendanceByStudentIdAndLectureId(String studentId, String lectureId);

}
