package udpm.hn.server.core.superadmin.activity.model.request;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.StatusUserActivity;

@Getter
@Setter
public class UserActivityLogFilterRequest extends PageableRequest {
    private EntityStatus status;
    private String nameOrCode;
    private String email;
    private Long fromDate;
    private Long toDate;
    private StatusUserActivity statusUserActivity;
}
