package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Department;
import udpm.hn.server.entity.Major;

import java.util.Optional;

@Repository
public interface MajorRepository extends JpaRepository<Major, String> {

    @Query("""
            SELECT m
            FROM Major m
            WHERE m.majorIdentityId = :majorIdentityId
            """)
    Optional<Major> findMajorByMajorIdentityId(Long majorIdentityId);

}
