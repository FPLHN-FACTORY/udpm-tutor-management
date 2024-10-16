package udpm.hn.server.infrastructure.config.job.staff.commonio;

import jakarta.transaction.Transactional;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import udpm.hn.server.entity.Role;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.StaffMajorFacility;
import udpm.hn.server.entity.StaffRole;
import udpm.hn.server.infrastructure.config.job.staff.model.dto.TransferStaffRole;
import udpm.hn.server.infrastructure.config.job.staff.repository.ConfigStaffCustomRepository;
import udpm.hn.server.infrastructure.config.job.staff.repository.ConfigStaffMajorFacilityRepository;
import udpm.hn.server.infrastructure.config.job.staff.repository.ConfigStaffRoleCustomRepository;
import udpm.hn.server.infrastructure.constant.EntityStatus;

import java.util.UUID;

@Component
@Slf4j
@Transactional
public class StaffWriter implements ItemWriter<TransferStaffRole> {

    @Setter(onMethod_ = {@Autowired})
    private ConfigStaffCustomRepository staffCustomRepository;

    @Setter(onMethod_ = {@Autowired})
    private ConfigStaffRoleCustomRepository staffRoleCustomRepository;

    @Setter(onMethod_ = {@Autowired})
    private ConfigStaffMajorFacilityRepository staffMajorFacilityRepository;

    @Override
    public void write(Chunk<? extends TransferStaffRole> chunk) throws Exception {
        int recordNumber = 0;

        for (TransferStaffRole transferStaffRole : chunk) {
            recordNumber++;
            try {
                Staff savedStaff = saveOrUpdateStaff(transferStaffRole.getStaff());
                StaffRole savedStaffRole = saveStaffRole(savedStaff, transferStaffRole.getRole());
                StaffMajorFacility savedStaffMajorFacility = saveStaffMajorFacility(savedStaff, transferStaffRole.getStaffMajorFacility());
                log.info(
                        "Successfully processed record number {}: Saved Staff Role - {}, Saved Staff Major Facility - {}",
                        recordNumber, savedStaffRole, savedStaffMajorFacility
                );
            } catch (Exception e) {
                log.error("Error processing record number {}: {}", recordNumber, transferStaffRole, e);
            }
        }
    }

    private Staff saveOrUpdateStaff(Staff staff) {
        return staffCustomRepository
                .findByStaffCodeAndStatus(staff.getStaffCode(), EntityStatus.ACTIVE)
                .orElseGet(() -> staffCustomRepository.save(staff));
    }

    private StaffRole saveStaffRole(Staff staff, Role role) {
        StaffRole staffRole = new StaffRole();
        staffRole.setStaff(staff);
        staffRole.setRole(role);
        staffRole.setId(UUID.randomUUID().toString());
        staffRole.setStatus(EntityStatus.ACTIVE);
        return staffRoleCustomRepository.save(staffRole);
    }

    private StaffMajorFacility saveStaffMajorFacility(Staff staff, StaffMajorFacility staffMajorFacility) {
        staffMajorFacility.setStaff(staff);
        return staffMajorFacilityRepository.save(staffMajorFacility);
    }
}
