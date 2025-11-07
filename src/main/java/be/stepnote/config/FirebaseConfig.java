package be.stepnote.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void init() throws Exception {
        if (!com.google.firebase.FirebaseApp.getApps().isEmpty()) return;
        var serviceAccount = new FileInputStream("src/main/resources/firebasekey/firebase-admin-key.json");
        var options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();
        com.google.firebase.FirebaseApp.initializeApp(options);
    }

}
