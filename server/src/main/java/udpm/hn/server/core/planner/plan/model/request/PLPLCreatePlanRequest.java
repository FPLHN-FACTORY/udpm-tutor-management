package udpm.hn.server.core.planner.plan.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import udpm.hn.server.core.common.base.PageableRequest;

@Setter
@Getter
@ToString
public class PLPLCreatePlanRequest{

    private String blockId;
    private String semesterId;
    private String departmentCode;
    private String facilityCode;
    private String userCode;
    private String description;
    private Long startTime;
    private Long endTime;

}
