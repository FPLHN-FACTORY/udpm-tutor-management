package udpm.hn.server.core.superadmin.activity.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import udpm.hn.server.entity.Staff;
import udpm.hn.server.infrastructure.constant.EntityStatus;
import udpm.hn.server.infrastructure.constant.StatusUserActivity;

@Getter
@Setter
public class UserActivityLogRequest {
    private Staff user;
    private Authentication authentication;
    private StatusUserActivity statusUserActivity;
    private EntityStatus status;
}
