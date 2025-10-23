package be.stepnote.member;

import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthMemberProvider {

    private final MemberRepository memberRepository;

    public Member getMember(String username) {
        return memberRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
    }
}
