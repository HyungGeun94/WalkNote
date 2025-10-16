package be.stepnote.config;

import be.stepnote.config.security.CustomOAuth2User;
import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityAuditorAware implements AuditorAware<Member> {

    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();




        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        //  principal이 문자열("anonymousUser")로 들어오는 경우도 있음
        if (principal instanceof CustomOAuth2User customUser) {
            String username = customUser.getUsername();
            return memberRepository.findByUsername(username);
        }

        //  폼 로그인 유저 등 다른 타입 고려
        if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
            return memberRepository.findByUsername(springUser.getUsername());
        }

        //  익명 유저거나 타입이 맞지 않는 경우
        return Optional.empty();



    }
}
