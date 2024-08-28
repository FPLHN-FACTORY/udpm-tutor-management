package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.HeadSubjectBySemester;

@Repository
public interface HeadSubjectBySemesterRepository extends JpaRepository<HeadSubjectBySemester, String> {
}
