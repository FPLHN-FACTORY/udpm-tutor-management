package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.ClassStudentJoined;

@Repository
public interface ClassStudentJoinedRepository extends JpaRepository<ClassStudentJoined, String> {
}
