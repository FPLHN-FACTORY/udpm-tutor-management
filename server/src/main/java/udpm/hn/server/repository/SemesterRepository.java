package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Semester;

import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, String> {

    @Query("""
            SELECT s
            FROM Semester s
            WHERE s.semesterId = :semesterId
            """)
    Optional<Semester> findBySemesterId(long semesterId);

}
