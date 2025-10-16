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

        CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();



        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        String username = principal.getUsername(); // principal
        return memberRepository.findByUsername(username);

    }
}
