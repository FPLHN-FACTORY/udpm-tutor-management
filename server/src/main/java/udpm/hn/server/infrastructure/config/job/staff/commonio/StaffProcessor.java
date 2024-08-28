package udpm.hn.server.infrastructure.config.job.staff.commonio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import udpm.hn.server.entity.DepartmentFacility;
import udpm.hn.server.entity.Role;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.config.job.staff.model.dto.TranferStaffRole;
import udpm.hn.server.infrastructure.config.job.staff.model.request.StaffExcelRequest;
import udpm.hn.server.infrastructure.config.job.staff.repository.DepartmentFacilityCustomRepository;
import udpm.hn.server.infrastructure.config.job.staff.repository.RoleCustomRepository;
import udpm.hn.server.infrastructure.config.job.staff.repository.StaffCustomRepository;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.utils.CodeGenerator;

import java.util.List;

@Component
@Slf4j
public class StaffProcessor implements ItemProcessor<StaffExcelRequest, TranferStaffRole> {

    @Autowired
    private RoleCustomRepository roleCustomRepository;

    @Autowired
    private StaffCustomRepository staffCustomRepository;

    @Autowired
    private DepartmentFacilityCustomRepository departmentFacilityRepository;

    @Override
    public TranferStaffRole process(StaffExcelRequest item) throws Exception {
        try {
            String staffCode = "";
            String departmentFacility = item.getDepartmentFacilityName();
            String departmentName = departmentFacility.split(" - ")[0];
            String dFacilityName = departmentFacility.split(" - ")[1];
            String roleFacility = item.getRoleFacilityName();
            String roleName = roleFacility.split(" - ")[0];
            String rFacilityName = roleFacility.split(" - ")[1];
            List<DepartmentFacility> departmentFacilities = departmentFacilityRepository.findByDepartmentFacility(departmentName, dFacilityName);
            List<Role> roles = roleCustomRepository.findAllByRoleNameAndFacilityName(roleName, rFacilityName);

            if (departmentFacilities.isEmpty()) {
                log.error("Bộ Môn Không Tồn Tại");
                return null;
            }

            if (roles.isEmpty()) {
                log.error("Chức Vụ Không Tồn Tại");
                return null;
            }

            if (item.getStaffCode() == null || item.getStaffCode().isEmpty()) {
                log.error("Mã Nhân Viên Không Được Để Trống");
                return null;
            } else if (!item.getEmailFe().contains(item.getStaffCode()) || !item.getEmailFpt().contains(item.getStaffCode())) {
                log.error("ID Giáo Viên Không Đúng Định Dạng");
                return null;
            } else {
                staffCode = item.getStaffCode();
            }

            List<Staff> staff = staffCustomRepository.findByStaffCode(staffCode);
            if (!staff.isEmpty()) {
                staff.get(0).setName(item.getName());
                staff.get(0).setEmailFpt(item.getEmailFpt());
                staff.get(0).setEmailFe(item.getEmailFe());
//                staff.get(0).setDepartmentFacility(departmentFacilities.get(0));
                staff.get(0).setStatus(EntityStatus.ACTIVE);
                return new TranferStaffRole(staff.get(0), roles.get(0));
            }

            Staff staffNew = new Staff();
            staffNew.setId(CodeGenerator.generateRandomCode());
            staffNew.setStaffCode(staffCode);
            staffNew.setName(item.getName());
            staffNew.setEmailFpt(item.getEmailFpt());
            staffNew.setEmailFe(item.getEmailFe());
//            staffNew.setDepartmentFacility(departmentFacilities.get(0));
            staffNew.setStatus(EntityStatus.ACTIVE);
            return new TranferStaffRole(staffNew, roles.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error processing excel row : " + item, e);
            return null;
        }
    }
}
