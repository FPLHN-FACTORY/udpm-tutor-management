package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.OperationLog;

@Repository
public interface OperationLogsRepository extends JpaRepository<OperationLog, String> {
}
