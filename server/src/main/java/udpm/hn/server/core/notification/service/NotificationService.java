package udpm.hn.server.core.notification.service;

import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.core.notification.model.request.ReadNotificationRequest;
import udpm.hn.server.core.notification.model.request.NotificationRequest;

public interface NotificationService {

    ResponseObject<?> getAllNotifications(NotificationRequest request);

    ResponseObject<?> readNotification(String id, ReadNotificationRequest request);
    ResponseObject<?> countNotification(NotificationRequest request);

}
