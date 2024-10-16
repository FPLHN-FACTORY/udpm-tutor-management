package udpm.hn.server.infrastructure.config.job.staff.commonio;

import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import udpm.hn.server.entity.MajorFacility;
import udpm.hn.server.entity.Role;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.entity.StaffMajorFacility;
import udpm.hn.server.infrastructure.config.job.staff.model.dto.TransferStaffRole;
import udpm.hn.server.infrastructure.config.job.staff.model.request.StaffExcelRequest;
import udpm.hn.server.infrastructure.config.job.staff.repository.ConfigMajorFacilityCustomRepository;
import udpm.hn.server.infrastructure.config.job.staff.repository.ConfigRoleCustomRepository;
import udpm.hn.server.infrastructure.config.job.staff.repository.ConfigStaffCustomRepository;
import udpm.hn.server.infrastructure.constant.EntityStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class StaffProcessor implements ItemProcessor<StaffExcelRequest, TransferStaffRole> {

    @Setter(onMethod_ = {@Autowired})
    private ConfigRoleCustomRepository roleCustomRepository;

    @Setter(onMethod_ = {@Autowired})
    private ConfigStaffCustomRepository staffCustomRepository;

    @Setter(onMethod_ = {@Autowired})
    private ConfigMajorFacilityCustomRepository majorFacilityCustomRepository;

    @Override
    public TransferStaffRole process(@NonNull StaffExcelRequest item) throws Exception {
        try {
            String[] departmentParts = item.getDepartmentFacilityName().split(" - ");
            String[] roleParts = item.getRoleFacilityName().split(" - ");

            if (departmentParts.length != 3 || roleParts.length != 2) {
                return null;
            }

            String departmentName = departmentParts[0];
            String majorName = departmentParts[1];
            String facilityCode = departmentParts[2];
            String roleName = roleParts[0];
            String rFacilityName = roleParts[1];

            Optional<MajorFacility> majorFacility = fetchMajorFacility(departmentName, majorName, facilityCode);
            Optional<Role> role = fetchRole(roleName, rFacilityName);

            if (majorFacility.isEmpty() || role.isEmpty()) {
                return null;
            }

            if (!isValidStaffCode(item)) {
                return null;
            }

            return createTransferStaffRole(item, majorFacility.get(), role.get());
        } catch (Exception e) {
            return null;
        }
    }

    private Optional<MajorFacility> fetchMajorFacility(String departmentName, String majorName, String facilityCode) {
        List<MajorFacility> majorFacilities = majorFacilityCustomRepository
                .getMajorFacilities(
                        departmentName,
                        majorName,
                        facilityCode
                );
        return majorFacilities.isEmpty() ? Optional.empty() : Optional.of(majorFacilities.get(0));
    }

    private Optional<Role> fetchRole(String roleName, String facilityName) {
        List<Role> roles = roleCustomRepository
                .findAllByRoleNameAndFacilityName(
                        roleName,
                        facilityName
                );
        return roles.isEmpty() ? Optional.empty() : Optional.of(roles.get(0));
    }

    private boolean isValidStaffCode(StaffExcelRequest item) {
        String staffCode = item.getStaffCode();
        if (staffCode == null || staffCode.isEmpty()) {
            return false;
        }
        return item.getAccountFe().toLowerCase().contains(staffCode.toLowerCase()) &&
               item.getAccountFpt().toLowerCase().contains(staffCode.toLowerCase());
    }

    private TransferStaffRole createTransferStaffRole(StaffExcelRequest item, MajorFacility majorFacility, Role role) {
        Staff staff = staffCustomRepository
                .findByStaffCodeAndStatus(item.getStaffCode(), EntityStatus.ACTIVE)
                .map(existingStaff -> updateExistingStaff(existingStaff, item))
                .orElseGet(() -> createNewStaff(item));

        StaffMajorFacility staffMajorFacility = new StaffMajorFacility();
        staffMajorFacility.setStatus(EntityStatus.ACTIVE);
        staffMajorFacility.setMajorFacility(majorFacility);

        return new TransferStaffRole(staff, role, staffMajorFacility);
    }

    private Staff updateExistingStaff(Staff staff, StaffExcelRequest item) {
        staff.setName(item.getName());
        staff.setEmailFpt(item.getAccountFpt());
        staff.setEmailFe(item.getAccountFe());
        return staff;
    }

    private Staff createNewStaff(StaffExcelRequest item) {
        Staff newStaff = new Staff();
        newStaff.setId(UUID.randomUUID().toString());
        newStaff.setStaffCode(item.getStaffCode());
        newStaff.setName(item.getName());
        newStaff.setEmailFpt(item.getAccountFpt());
        newStaff.setEmailFe(item.getAccountFe());
        newStaff.setStatus(EntityStatus.ACTIVE);
        return newStaff;
    }

}
