package udpm.hn.server.core.admin.staff.model.request;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;

@Getter
@Setter
public class HOStaffRequest extends PageableRequest {

    private String searchQuery;

}
