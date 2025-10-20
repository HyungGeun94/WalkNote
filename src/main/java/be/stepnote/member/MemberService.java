package be.stepnote.member;

import be.stepnote.config.security.CustomOAuth2User;
import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberInfoUpdateRequest;
import be.stepnote.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberInfoResponse userInfo() {

        CustomOAuth2User user = (CustomOAuth2User) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        String username = user.getUsername();

        Optional<Member> byUsername = memberRepository.findByUsername(username);

        Member member = byUsername.orElseThrow();

        return MemberInfoResponse.from(member);


    }

    public void updateUserInfo(MemberInfoUpdateRequest request) {
        CustomOAuth2User user = (CustomOAuth2User) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

        String username = user.getUsername();

        Optional<Member> byUsername = memberRepository.findByUsername(username);

        Member member = byUsername.orElseThrow();

        member.updatePartial(request.getBio(),request.getNickname(),request.getImageUrl());




    }
}
