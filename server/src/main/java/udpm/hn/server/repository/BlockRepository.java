package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Block;

@Repository
public interface BlockRepository extends JpaRepository<Block, String> {
}
