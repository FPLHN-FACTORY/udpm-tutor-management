package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String> {
}
