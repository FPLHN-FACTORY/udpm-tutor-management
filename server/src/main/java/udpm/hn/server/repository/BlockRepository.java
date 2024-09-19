package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Block;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block, String> {

    @Query("""
            SELECT b
            FROM Block b
            WHERE b.semester.id = :semesterId
            """)
    List<Block> findBlockBySemesterId(String semesterId);

}
