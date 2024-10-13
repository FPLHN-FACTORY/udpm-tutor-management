package udpm.hn.server.core.notification.model.request;

import lombok.Getter;
import lombok.Setter;
import udpm.hn.server.core.common.base.PageableRequest;

@Getter
@Setter
public class NotificationRequest extends PageableRequest {

    private String keyWord;

    private String departmentCode;

    private String facilityCode;

    private String userId;


}
