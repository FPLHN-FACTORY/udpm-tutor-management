package udpm.hn.server.core.planner.plan.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import udpm.hn.server.core.common.base.PageableRequest;

@Setter
@Getter
@ToString
public class PLPLTutorClassDetailRequest extends PageableRequest {

    private String planId;
    private String teacherId;
    private String query;
    private String semesterId;
    private String facilityId;

}
