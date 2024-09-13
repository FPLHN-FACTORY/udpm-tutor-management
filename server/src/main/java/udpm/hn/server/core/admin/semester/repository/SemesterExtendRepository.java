package udpm.hn.server.core.admin.semester.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.semester.model.request.SemesterRequest;
import udpm.hn.server.core.admin.semester.model.response.DetailSemesterResponse;
import udpm.hn.server.core.admin.semester.model.response.SemesterResponse;
import udpm.hn.server.entity.Semester;
import udpm.hn.server.repository.SemesterRepository;

import java.util.Optional;

@Repository
public interface SemesterExtendRepository extends SemesterRepository {

    @Query(
            value = """
                    SELECT
                    s.id AS id,
                    ROW_NUMBER() over (ORDER BY s.id DESC) AS orderNumber,
                    s.name AS semesterName,
                    s.year AS semesterYear,
                    s.start_time AS startTime,
                    s.end_time AS endTime,
                    MAX(CASE WHEN b.name = 'BLOCK_1' THEN b.start_time END) AS startTimeFirstBlock,
               		MAX(CASE WHEN b.name = 'BLOCK_1' THEN b.end_time END) AS endTimeFirstBlock,
               		MAX(CASE WHEN b.name = 'BLOCK_2' THEN b.start_time END) AS startTimeSecondBlock,
               		MAX(CASE WHEN b.name = 'BLOCK_2' THEN b.end_time END) AS endTimeSecondBlock
                    FROM
                        semester s
                        JOIN block b ON b.semester_id = s.id
                    WHERE
                         (:#{#request.semesterName} IS NULL OR s.name LIKE %:#{#request.semesterName}%)
                        AND (:#{#request.semesterYear} IS NULL OR s.year = :#{#request.semesterYear})
                    GROUP BY
               			s.id, s.name, s.start_time, s.end_time
                    """,
            countQuery = """
                        SELECT
                            COUNT(s.id)
                        FROM
                            semester s
                        WHERE
                             (:#{#request.semesterName} IS NULL OR s.name LIKE %:#{#request.semesterName}%)
                            AND (:#{#request.semesterYear} IS NULL OR s.year <= :#{#request.semesterYear})
                    """,
            nativeQuery = true
    )
    Page<SemesterResponse> getAllSemester(Pageable pageable, SemesterRequest request);

    @Query(
            value = """
                    SELECT
                        s.id AS id,
                        s.name AS semesterName,
                        s.year AS semesterYear,
                        s.start_time AS startTime,
                        s.end_time AS endTime,
                        MAX(CASE WHEN b.name = 'BLOCK_1' THEN b.start_time END) AS startTimeFirstBlock,
                        MAX(CASE WHEN b.name = 'BLOCK_1' THEN b.end_time END) AS endTimeFirstBlock,
                        MAX(CASE WHEN b.name = 'BLOCK_2' THEN b.start_time END) AS startTimeSecondBlock,
                        MAX(CASE WHEN b.name = 'BLOCK_2' THEN b.end_time END) AS endTimeSecondBlock
                    FROM
                        semester s
                    JOIN block b ON b.semester_id = s.id
                    WHERE
                        s.id = :id
                    GROUP BY
               			s.id, s.name, s.start_time, s.end_time
                    """,
            nativeQuery = true
    )
    Optional<DetailSemesterResponse> getDetailSemesterById(String id);

}
