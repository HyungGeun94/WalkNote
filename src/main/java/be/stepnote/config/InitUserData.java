package be.stepnote.config;

import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitUserData {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void init() {
        String username = "test";

        if(memberRepository.existsByUsername(username)) {
            return;
        }

        Member member = Member.builder()
            .username(username)
            .nickname("stepnote")
            .name("stepnote")
            .password(bCryptPasswordEncoder.encode("1234"))
            .role("ROLE_USER")
            .build();
        member.updateFcmToken("fh4g1dCzQ8ar3kKXhZzaRo:APA91bHpnHUOeZrBY3JFkE230SJDAr1C--GbhoAC-nkQ6CbGID3fkJiWTNn-i-ssoNiLCtJp501I9Bu8AaWuJuzfNH13bPwnM0L5H4hpquh2AU4sIOIodqo");

        memberRepository.save(member);

    }
}
