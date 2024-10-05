package udpm.hn.server.core.headdepartment.plan.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class HDPLPlanInfoRequest {

    private String semesterId;

    private String departmentCode;

    private String facilityCode;

}
