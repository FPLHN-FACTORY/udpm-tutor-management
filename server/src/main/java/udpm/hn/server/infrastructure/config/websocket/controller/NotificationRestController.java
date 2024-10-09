package udpm.hn.server.infrastructure.config.websocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import udpm.hn.server.infrastructure.config.websocket.model.request.NotificationRequest;
import udpm.hn.server.infrastructure.config.websocket.model.request.ReadNotificationRequest;
import udpm.hn.server.infrastructure.config.websocket.service.NotificationService;
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
