package udpm.hn.server.core.admin.role.model.request;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;

@Getter
@Setter
public class HORoleRequest extends PageableRequest {

    private String roleName;

    private String idFacility;

}
