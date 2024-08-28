package udpm.hn.server.infrastructure.config.job.staff.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaffExcelRequest {

    private String name;

    private String staffCode;

    private String emailFe;

    private String emailFpt;

    private String departmentFacilityName;

    private String roleFacilityName;

}
