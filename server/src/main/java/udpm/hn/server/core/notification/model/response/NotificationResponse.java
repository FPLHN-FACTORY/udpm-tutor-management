package udpm.hn.server.core.notification.model.response;

import udpm.hn.server.core.common.base.IsIdentify;

public interface NotificationResponse extends IsIdentify {

    String getContent();

    Boolean getStatus();

    Long getCreatedDate();


}
