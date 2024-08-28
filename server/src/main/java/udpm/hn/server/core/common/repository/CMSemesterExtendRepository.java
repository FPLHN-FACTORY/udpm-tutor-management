package udpm.hn.server.core.common.repository;

import org.springframework.data.jpa.repository.Query;
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
                        CONCAT(s.name, ' - ', s.year) AS semesterInfo
                    FROM
                        semester s
                    ORDER BY s.start_time DESC
                    """,
            nativeQuery = true
    )
    List<SemesterInfoResponse> getSemesterInfos();

}
