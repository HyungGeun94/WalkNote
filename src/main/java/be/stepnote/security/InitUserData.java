package be.stepnote.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitUserData {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void init() {
        String username = "test";

        // 이미 존재하면 스킵
        if (userRepository.findByUsername(username) != null) {
            return;
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode("1234"));
        user.setRole("ROLE_USER");

        userRepository.save(user);

        System.out.println("✅ [InitUserData] test 계정 자동 생성 완료 (id=" + user.getId() + ")");
    }
}
