package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
}
