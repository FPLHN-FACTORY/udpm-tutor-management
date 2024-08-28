package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {
}
