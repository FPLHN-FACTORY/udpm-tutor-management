package udpm.hn.server.core.planner.plan.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import udpm.hn.server.core.common.base.PageableRequest;

@Setter
@Getter
@ToString
public class PLPLPlanListRequest extends PageableRequest {

    private String semesterId;

    private String departmentCode;

    private String facilityCode;

    private String planStatus;

}
