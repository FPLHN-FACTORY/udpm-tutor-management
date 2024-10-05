package udpm.hn.server.core.planner.plan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.planner.plan.model.request.PLPLSubjectListRequest;
import udpm.hn.server.core.planner.plan.model.response.PLPLTutorClassResponse;
import udpm.hn.server.entity.StudentTutor;
import udpm.hn.server.repository.StudentTutorRepository;
import udpm.hn.server.repository.TutorClassRepository;

import java.util.Optional;

@Repository
public interface PLPLStudentTutorClassRepository extends StudentTutorRepository {

    Optional<StudentTutor> findByStudentCode(String studentCode);
    Optional<StudentTutor> findByStudentEmail(String mail);

}
