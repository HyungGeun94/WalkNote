package be.stepnote.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmTestController {
    private final FirebaseService firebaseService;

    @PostMapping("/send")
    public String send(@RequestParam String token) {
        firebaseService.send("StepNote 알림", "잘 가나요???", token);
        return "ok";
    }
}
