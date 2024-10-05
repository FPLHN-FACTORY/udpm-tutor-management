package udpm.hn.server.core.common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.common.model.response.SemesterInfoResponse;
import udpm.hn.server.core.common.model.response.SubjectOptionsResponse;
import udpm.hn.server.repository.SubjectRepository;

import java.util.List;

@Repository
public interface CMSubjectExtendResponse extends SubjectRepository {

    @Query(
            value = """
                    SELECT
                        s.id AS id,
                        s.name AS name 
                    FROM
                        subject s
                    ORDER BY s.created_date 
                    """,
            nativeQuery = true
    )
    List<SubjectOptionsResponse> getSubjectOptions();

}
