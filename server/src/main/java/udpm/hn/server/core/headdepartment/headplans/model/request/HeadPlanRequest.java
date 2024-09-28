package udpm.hn.server.core.headdepartment.headplans.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import udpm.hn.server.core.common.base.PageableRequest;

@Getter
@Setter
@ToString
public class HeadPlanRequest extends PageableRequest {

    private String semesterId;

    private String planStatus;

}
