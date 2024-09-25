package udpm.hn.server.infrastructure.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Role;
import udpm.hn.server.repository.RoleRepository;
import udpm.hn.server.repository.SemesterRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterAuthRepository extends SemesterRepository {

    @Query(
            value = """
                      SELECT s.id FROM semester s WHERE :now BETWEEN s.start_time AND s.end_time
                      """,
            nativeQuery = true)
    String findSemesterBy(@Param("now") Long now);

}
