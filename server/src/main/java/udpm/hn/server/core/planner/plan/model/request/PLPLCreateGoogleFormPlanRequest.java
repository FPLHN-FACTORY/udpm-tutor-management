package udpm.hn.server.core.planner.plan.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PLPLCreateGoogleFormPlanRequest {

    private String planId;
    private String departmentCode;
    private String facilityCode;

}
