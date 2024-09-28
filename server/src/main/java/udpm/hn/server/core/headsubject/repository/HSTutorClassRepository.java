package udpm.hn.server.core.headsubject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import udpm.hn.server.core.headsubject.model.response.TutorClassResponse;
import udpm.hn.server.core.planner.plan.model.response.PLPLTutorClassResponse;
import udpm.hn.server.repository.TutorClassRepository;

import java.util.List;

@Repository
public interface HSTutorClassRepository extends TutorClassRepository {



}
