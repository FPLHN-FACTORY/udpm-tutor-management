package udpm.hn.server.core.headsubject.plan.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class HSPLPlanInfoRequest {

    private String semesterId;
    private String departmentCode;
    private String facilityCode;

}
