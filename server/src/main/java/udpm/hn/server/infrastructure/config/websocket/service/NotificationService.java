package udpm.hn.server.infrastructure.config.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendNotification(String destination, Object message) {
        simpMessagingTemplate.convertAndSend(destination, message);
    }

}
