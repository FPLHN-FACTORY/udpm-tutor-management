package udpm.hn.server.core.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udpm.hn.server.core.notification.model.request.ReadNotificationRequest;
import udpm.hn.server.core.notification.model.request.NotificationRequest;
import udpm.hn.server.core.notification.service.NotificationService;
import udpm.hn.server.infrastructure.constant.MappingConstants;
import udpm.hn.server.utils.Helper;

@RestController
@RequestMapping(MappingConstants.API_NOTIFICATION)
@RequiredArgsConstructor
public class NotificationRestController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getAllNotification(final NotificationRequest request) {
        return Helper.createResponseEntity(notificationService.getAllNotifications(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> readNotification(@PathVariable String id, @RequestBody ReadNotificationRequest request) {
        return Helper.createResponseEntity(notificationService.readNotification(id, request));
    }

    @GetMapping("/count")
    public ResponseEntity<?> countNotification(final NotificationRequest request) {
        return Helper.createResponseEntity(notificationService.countNotification(request));
    }
}
