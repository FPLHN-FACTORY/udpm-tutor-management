package udpm.hn.server.core.headdepartment.plan.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import udpm.hn.server.core.common.base.PageableRequest;

@Setter
@Getter
@ToString
public class HDPLSubjectListRequest extends PageableRequest {

    private String planId;
    private String facilityId;
    private String semesterId;

}
