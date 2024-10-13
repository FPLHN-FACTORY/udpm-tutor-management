package udpm.hn.server.infrastructure.config.database.repository;

import org.springframework.stereotype.Repository;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.entity.Role;
import udpm.hn.server.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DBGenRoleRepository extends RoleRepository {

    Optional<Role> findByCodeAndNameAndFacility_Id(String code, String name, String facilityId);

    List<Role> findAllByFacility_Id(String facilityId);

    Optional<Role> findByCodeAndFacility(String code, Facility facility);

}
