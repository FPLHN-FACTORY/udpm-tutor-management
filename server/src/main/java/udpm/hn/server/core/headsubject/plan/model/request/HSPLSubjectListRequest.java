package udpm.hn.server.core.headsubject.plan.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import udpm.hn.server.core.common.base.PageableRequest;

@Setter
@Getter
@ToString
public class HSPLSubjectListRequest extends PageableRequest {

    private String planId;
    private String staffId;
    private String facilityId;
    private String semesterId;

}
