package be.stepnote.alarm;

import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {
    public void send(String title, String body, String token) {
        try {
//            var msg = com.google.firebase.messaging.Message.builder()
//                .setToken(token)
//                .setNotification(
//                    com.google.firebase.messaging.Notification.builder()
//                        .setTitle(title).setBody(body).build()
//                ).build();
            Message msg = Message.builder()
                .setToken(token)
                .putData("title", title)
                .putData("body", body)
                .build();
            com.google.firebase.messaging.FirebaseMessaging.getInstance().send(msg);
        } catch (com.google.firebase.messaging.FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
