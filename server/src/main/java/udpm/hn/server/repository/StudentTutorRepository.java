package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.StudentTutor;

@Repository
public interface StudentTutorRepository extends JpaRepository<StudentTutor, String> {
}
