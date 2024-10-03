package udpm.hn.server.core.common.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Setter
@Getter
@ToString
public class StaffSearchByRoleRequest {

    private List<String> role;
    private String departmentCode;
    private String facilityCode;

}
