package udpm.hn.server.infrastructure.config.job.staff.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import udpm.hn.server.entity.Role;
import udpm.hn.server.entity.Staff;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TranferStaffRole {

    private Staff staff;

    private Role role;

}
