package udpm.hn.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.PlanLogHistory;

@Repository
public interface PlanLogHistoryRepository extends JpaRepository<PlanLogHistory, String> {
}