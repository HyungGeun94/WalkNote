package be.stepnote.member;

import be.stepnote.config.security.CustomOAuth2User;
import be.stepnote.follow.FollowRepository;
import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberInfoUpdateRequest;
import be.stepnote.member.repository.MemberRepository;
import be.stepnote.report.favorite.WalkReportFavoriteRepository;
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

    private final AuthMemberProvider authMemberProvider;

    private final WalkReportFavoriteRepository walkReportFavoriteRepository;

    private final FollowRepository followRepository;

    public MemberInfoResponse userInfo() {

        Member member = authMemberProvider.getCurrentMember();

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

    public MemberCountsResponse getCounts() {

        Member member = authMemberProvider.getCurrentMember();

        Long favoriteCount = walkReportFavoriteRepository.countByMember(member);

        Long followingCount = followRepository.countByFollower(member);

        Long followerCount = followRepository.countByFollowing(member);

        return new MemberCountsResponse(favoriteCount, followerCount, followingCount);
    }
}

