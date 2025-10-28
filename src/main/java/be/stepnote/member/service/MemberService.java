package be.stepnote.member.service;

import be.stepnote.follow.FollowRepository;
import be.stepnote.member.AuthMemberProvider;
import be.stepnote.member.dto.MemberCountsResponse;
import be.stepnote.member.dto.MemberInfoResponse;
import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberInfoUpdateRequest;
import be.stepnote.report.favorite.WalkReportFavoriteRepository;
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

    private final AuthMemberProvider authMemberProvider;

    private final WalkReportFavoriteRepository walkReportFavoriteRepository;

    private final FollowRepository followRepository;



    public MemberInfoResponse userInfo() {

        Member member = authMemberProvider.getCurrentMember();

        return MemberInfoResponse.from(member);


    }

    public void updateUserInfo(MemberInfoUpdateRequest request) {

        Member member = authMemberProvider.getCurrentMember();

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

