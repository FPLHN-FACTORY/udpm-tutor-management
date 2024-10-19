package udpm.hn.server.infrastructure.config.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Value("${ws.topicPrefix}")
    private String topicPrefix;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendNotification(String destination, Object message) {
        simpMessagingTemplate.convertAndSend(destination, message);
    }

    public void sendNotification(Object message) {
        simpMessagingTemplate.convertAndSend(topicPrefix, message);
    }

}
