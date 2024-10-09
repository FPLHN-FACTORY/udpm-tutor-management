package udpm.hn.server.infrastructure.config.websocket.model.response;

import udpm.hn.server.core.common.base.IsIdentify;

public interface NotificationResponse extends IsIdentify {

    String getContent();

    Boolean getStatus();

    Long getCreatedDate();

}
