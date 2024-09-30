package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.TutorClass;

@Repository
public interface TutorClassRepository extends JpaRepository<TutorClass, String> {
}
