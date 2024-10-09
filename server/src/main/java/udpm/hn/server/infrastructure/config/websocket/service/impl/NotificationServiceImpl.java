package udpm.hn.server.infrastructure.config.websocket.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import udpm.hn.server.core.common.base.PageableObject;
import udpm.hn.server.core.common.base.ResponseObject;
import udpm.hn.server.entity.Notification;
import udpm.hn.server.infrastructure.config.websocket.model.request.NotificationRequest;
import udpm.hn.server.infrastructure.config.websocket.model.request.ReadNotificationRequest;
import udpm.hn.server.infrastructure.config.websocket.repository.NotificationExtendRepository;
import udpm.hn.server.infrastructure.config.websocket.service.NotificationService;
import udpm.hn.server.utils.Helper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationExtendRepository notificationExtendRepository;

    @Override
    public ResponseObject<?> getAllNotifications(NotificationRequest request) {
        Pageable pageable = Helper.createPageable(request);
        return new ResponseObject<>(
                PageableObject.of(notificationExtendRepository.getAllNotifications(pageable, request)),
                HttpStatus.OK,
                "Lấy thông báo thành công"
        );
    }

    @Override
    public ResponseObject<?> readNotification(String id, ReadNotificationRequest request) {
        Optional<Notification> notificationOptional = notificationExtendRepository.findById(id);
        if (notificationOptional.isEmpty()) {
            return new ResponseObject<>(null, HttpStatus.BAD_REQUEST, "Thông báo không tồn tại");
        }

        notificationOptional.map(notification -> {
            //Lưu lại các user_id đã đọc thông báo
            if (notification.getUserReceived() == null) {
                notification.setUserReceived(request.getUserId());
            } else {
                notification.setUserReceived(notification.getUserReceived() + ", " + request.getUserId());
            }
            return notificationExtendRepository.save(notification);
        });

        return notificationOptional
                .map(notification -> new ResponseObject<>(null, HttpStatus.OK, "Đọc thông báo thành công"))
                .orElseGet(() -> new ResponseObject<>(null, HttpStatus.BAD_GATEWAY, "Thất bại"));
    }

    @Override
    public ResponseObject<?> countNotification(NotificationRequest request) {
        return new ResponseObject<>(
                notificationExtendRepository.countNotification(request),
                HttpStatus.OK,
                "Lấy số lượng thông báo thành công"
        );
    }
}
