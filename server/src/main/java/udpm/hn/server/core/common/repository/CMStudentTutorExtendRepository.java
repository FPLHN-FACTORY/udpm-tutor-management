package udpm.hn.server.core.common.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.common.model.request.StudentSearchRequest;
import udpm.hn.server.core.common.model.response.CMOptionsResponse;
import udpm.hn.server.repository.StudentTutorRepository;

import java.util.List;

@Repository
public interface CMStudentTutorExtendRepository extends StudentTutorRepository {

    @Query(
            value = """
            SELECT
                st.id as id,
                CONCAT(st.student_code, ' - ', st.student_email) as name
            FROM
                student_tutor st
            """,
            nativeQuery = true
    )
    List<CMOptionsResponse> getStudentTutor(StudentSearchRequest request);

}
