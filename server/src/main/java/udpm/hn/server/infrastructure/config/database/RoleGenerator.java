package udpm.hn.server.infrastructure.config.database;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import udpm.hn.server.entity.Facility;
import udpm.hn.server.infrastructure.config.database.repository.DBGenFacilityRepository;
import udpm.hn.server.infrastructure.config.database.repository.DBGenRoleRepository;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.Role;

import java.util.List;

@Component
@Order(1)
@RequiredArgsConstructor
public class RoleGenerator {

    private final DBGenRoleRepository roleRepository;

    private final DBGenFacilityRepository facilityRepository;

    @PostConstruct
    public void generate() {
        List<Facility> listFacility = facilityRepository.findAll();
        for (Facility facility : listFacility) {
            List<udpm.hn.server.entity.Role> isHasRole = roleRepository.findAllByFacility(facility);
            if (isHasRole.isEmpty()) {
                List<String> roleCodes = Role.getAllRoles();
                List<String> roleNames = List.of("Admin", "Trưởng Môn", "Giảng Viên", "Sinh Viên", "Chủ Nhiệm Bộ Môn");
                for (int i = 0; i < roleCodes.size(); i++) {
                    if (!"Admin".equals(roleNames.get(i)) && roleRepository.findByCodeAndNameAndFacility(
                            roleCodes.get(i),
                            roleNames.get(i),
                            facility
                    ).isEmpty()) {
                        udpm.hn.server.entity.Role role = new udpm.hn.server.entity.Role();
                        role.setCode(roleCodes.get(i));
                        role.setName(roleNames.get(i));
                        role.setFacility(facility);
                        role.setStatus(EntityStatus.ACTIVE);
                        roleRepository.save(role);
                    }
                }
            }
        }
    }

}
