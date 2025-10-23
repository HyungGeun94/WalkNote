package be.stepnote.member;

import be.stepnote.config.security.CustomOAuth2User;
import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthMemberProvider {

    private final MemberRepository memberRepository;

    /** username으로 멤버 조회 (직접 주입형) */
    public Member getMember(String username) {
        return memberRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
    }

    /** 현재 로그인한 멤버 조회 (보안 컨텍스트형) */
    public Member getCurrentMember() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomOAuth2User user) {
            return getMember(user.getUsername());
        }
        throw new IllegalStateException("인증된 사용자 정보가 없습니다.");
    }
}