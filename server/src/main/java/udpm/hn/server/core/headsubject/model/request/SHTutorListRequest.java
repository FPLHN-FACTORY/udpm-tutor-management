package udpm.hn.server.core.headsubject.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import udpm.hn.server.core.common.base.PageableRequest;

@Setter
@Getter
@ToString
public class SHTutorListRequest  extends PageableRequest {
    private String planId;
    private String departmentCode;
    private String facilityCode;
    private String semesterId;
}
