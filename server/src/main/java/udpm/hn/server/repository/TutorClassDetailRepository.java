package udpm.hn.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.headsubject.plan.model.response.HSPLTutorClassDetailResponse;
import udpm.hn.server.entity.TutorClass;
import udpm.hn.server.entity.TutorClassDetail;

import java.util.List;

@Repository
public interface TutorClassDetailRepository extends JpaRepository<TutorClassDetail, String> {
}
