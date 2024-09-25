package udpm.hn.server.core.common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.common.model.response.SemesterInfoResponse;
import udpm.hn.server.repository.SemesterRepository;

import java.util.List;

@Repository
public interface CMSemesterExtendRepository extends SemesterRepository {

    @Query(
            value = """
                    SELECT
                        s.id AS id,
                        CONCAT(s.name, ' - ', s.year) AS name 
                    FROM
                        semester s
                    ORDER BY s.start_time 
                    """,
            nativeQuery = true
    )
    List<SemesterInfoResponse> getSemesterInfos();

    @Query(
            value = """
                    SELECT
                        s.id AS id,
                        s.name AS name 
                    FROM
                        block s
                    WHERE
                        s.semester_id = :id
                    ORDER BY s.start_time 
                    """,
            nativeQuery = true
    )
    List<SemesterInfoResponse> getBlockInfos(@Param("id") String id);

}
