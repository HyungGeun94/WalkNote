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
            .password(bCryptPasswordEncoder.encode("1234"))
            .role("ROLE_USER")
            .build();

        memberRepository.save(member);

    }
}
