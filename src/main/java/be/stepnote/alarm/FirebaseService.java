package be.stepnote.alarm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FirebaseService {

    @Async  //  비동기 처리
    public void send(String title, String body, String token) {
        try {

            Message msg = Message.builder()
                .setToken(token)
                .putData("title", title)
                .putData("body", body)
                .build();

            FirebaseMessaging.getInstance().send(msg);

        } catch (com.google.firebase.messaging.FirebaseMessagingException e) {
            log.error("[FCM] 실패: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }catch (Exception e) {
            log.error("[FCM] 예기치 못한 오류: {}", e.getMessage(), e);
        }

    }
}
