package udpm.hn.server.infrastructure.config.job.student.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Plan;
import udpm.hn.server.entity.Student;
import udpm.hn.server.entity.TutorClassDetail;
import udpm.hn.server.repository.StudentRepository;
import udpm.hn.server.repository.TutorClassDetailRepository;

import java.util.Optional;

@Repository
public interface ConfigTutorClassDetailCustomRepository extends TutorClassDetailRepository {

    Optional<TutorClassDetail> findByCode(String code);
    Optional<TutorClassDetail> findByCodeAndTutorClass_Plan(String code, Plan plan);

}
