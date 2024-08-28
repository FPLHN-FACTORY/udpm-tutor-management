package udpm.hn.server.core.admin.block.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.admin.block.model.response.BlockResponse;
import udpm.hn.server.entity.Block;
import udpm.hn.server.infrastructure.constant.BlockName;
import udpm.hn.server.repository.BlockRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockExtendRepository extends BlockRepository {

    @Query(
            value = """
                    SELECT
                    	b.id as id,
                    	b.semester_id as semesterId,
                    	b.name as blockName,
                    	b.start_time as startTime,
                    	b.end_time as endTime,
                    	b.status as blockStatus
                    FROM
                    	block b
                    WHERE
                    	b.semester_id = :semesterId
                    """, nativeQuery = true
    )
    List<BlockResponse> getAllBlockBySemesterId(String semesterId);

    Optional<Block> findBlockByNameAndSemesterId(BlockName name, String semester_id);

    List<Block> findAllBySemesterId(String semesterId);

    @Query(value = """
                SELECT
                    b.id,
                    b.semester_id,
                    b.name,
                    b.start_time,
                    b.end_time,
                    b.created_date,
                    b.last_modified_date,
                    b.status
                FROM block b
                WHERE b.name = :blockName
                AND b.semester_id = :semesterId
            """, nativeQuery = true)
    Optional<Block> existingByBlockAndSemester(String blockName, String semesterId);

    @Query(
            value = """
                    SELECT
                    	b.id as id,
                    	b.semester_id as semesterId,
                    	b.name as blockName,
                    	b.start_time as startTime,
                    	b.end_time as endTime,
                    	b.status as blockStatus
                    FROM
                    	block b
                    WHERE
                    	b.id = :blockId
                    """, nativeQuery = true
    )
    Optional<BlockResponse> getDetailBlockById(String blockId);
}