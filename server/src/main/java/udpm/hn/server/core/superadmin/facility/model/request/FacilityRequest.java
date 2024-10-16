package udpm.hn.server.core.superadmin.facility.model.request;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;

@Getter
@Setter
public class FacilityRequest extends PageableRequest {

    private String name;

    private String status;

}
