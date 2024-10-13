package udpm.hn.server.infrastructure.config.websocket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import udpm.hn.server.infrastructure.config.websocket.service.NotificationService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketMessageController {

    private final NotificationService notificationService;

    @MessageMapping("/send")
    @SendTo("/topic/notifications")
    public String send(String message) {
        notificationService.sendNotification("/topic/notifications", message);
        return message;
    }

}
