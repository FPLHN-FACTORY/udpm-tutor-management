package udpm.hn.server.infrastructure.config.job.student.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Student;
import udpm.hn.server.repository.StudentRepository;

import java.util.Optional;

@Repository
public interface ConfigStudentCustomRepository extends StudentRepository {

    Optional<Student> findByStudentCode(String studentCode);
}
