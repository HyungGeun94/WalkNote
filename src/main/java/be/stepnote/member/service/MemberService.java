package be.stepnote.member.service;

import be.stepnote.follow.FollowRepository;
import be.stepnote.member.AuthMemberProvider;
import be.stepnote.member.dto.MemberCountsResponse;
import be.stepnote.member.dto.MemberInfoResponse;
import be.stepnote.member.dto.NotificationSettingRequest;
import be.stepnote.member.entity.Member;
import be.stepnote.member.entity.NotificationSetting;
import be.stepnote.member.repository.MemberInfoUpdateRequest;
import be.stepnote.member.repository.NotificationSettingRepository;
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

    private final NotificationSettingRepository notificationSettingRepository;




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

    @Transactional
    public void updateFcmToken(String token) {
        Member member = authMemberProvider.getCurrentMember();
        member.updateFcmToken(token);
    }


    @Transactional
    public void update(NotificationSettingRequest dto) {
        Member member = authMemberProvider.getCurrentMember();

        NotificationSetting setting = notificationSettingRepository
            .findByMember(member)
            .orElseThrow(() -> new IllegalStateException("알림 설정이 존재하지 않습니다."));

        setting.update(
            dto.isLikeEnabled(),
            dto.isCommentEnabled(),
            dto.isSaveEnabled(),
            dto.isFollowEnabled()
        );
    }

    public void getProfile(String userId) {

    }
}

