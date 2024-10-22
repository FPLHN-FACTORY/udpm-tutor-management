package udpm.hn.server.infrastructure.config.job.student.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.ClassStudentJoined;
import udpm.hn.server.repository.ClassStudentJoinedRepository;

import java.util.Optional;

@Repository
public interface ConfigClassStudentJoinCustomRepository extends ClassStudentJoinedRepository {

    Optional<ClassStudentJoined> findByTutorClassDetail_IdAndStudent_StudentCode(String tutorClassDetailId, String studentCode);

}
