package udpm.hn.server.core.admin.staff.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HOStaffRoleRequest extends PageableRequest {

    private String staffId;

    private String roleName;

    private String idFacility;
}
