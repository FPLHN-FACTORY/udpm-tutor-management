package udpm.hn.server.infrastructure.config.websocket.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.infrastructure.config.websocket.model.request.NotificationRequest;
import udpm.hn.server.infrastructure.config.websocket.model.request.ReadNotificationRequest;

public interface NotificationService {

    ResponseObject<?> getAllNotifications(NotificationRequest request);

    ResponseObject<?> readNotification(String id, ReadNotificationRequest request);

    ResponseObject<?> countNotification(NotificationRequest request);

}
