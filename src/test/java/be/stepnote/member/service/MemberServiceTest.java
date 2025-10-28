package be.stepnote.member.service;


import be.stepnote.follow.FollowRepository;
import be.stepnote.member.AuthMemberProvider;
import be.stepnote.member.dto.MemberCountsResponse;
import be.stepnote.member.dto.MemberInfoResponse;
import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberInfoUpdateRequest;
import be.stepnote.report.favorite.WalkReportFavoriteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {


    @Mock
    AuthMemberProvider authMemberProvider;

    @Mock
    WalkReportFavoriteRepository walkReportFavoriteRepository;

    @Mock
    FollowRepository followRepository;


    @InjectMocks
    private MemberService memberService;

    @Test
    void userInfoTest() throws Exception {

        given(authMemberProvider.getCurrentMember()).willReturn(
            Member.create("username", "nickname"));

        MemberInfoResponse memberInfoResponse = memberService.userInfo();

        assertNotNull(memberInfoResponse);
        assertThat(memberInfoResponse.getNickname()).isEqualTo("nickname");


        verify(authMemberProvider, times(1)).getCurrentMember();


    }

    @Test
    void updateUserInfoTest() throws Exception {

        Member member = Member.create("username", "nickname");
        Member spyMember = Mockito.spy(member);
        given(authMemberProvider.getCurrentMember()).willReturn(spyMember);

        MemberInfoUpdateRequest request = new MemberInfoUpdateRequest("nickname2","imageUrl2","bio2");


        memberService.updateUserInfo(request);

        verify(authMemberProvider, times(1)).getCurrentMember();

        verify(spyMember, times(1))
            .updatePartial("bio2", "nickname2", "imageUrl2");

    }


    @Test
    void getCounts() throws Exception {
        given(authMemberProvider.getCurrentMember()).willReturn(
            Member.create("username", "nickname"));

        given(walkReportFavoriteRepository.countByMember(any(Member.class))).willReturn(1L);

        given(followRepository.countByFollower(any(Member.class))).willReturn(2L);

        given(followRepository.countByFollowing(any(Member.class))).willReturn(3L);

        MemberCountsResponse counts = memberService.getCounts();

        assertThat(counts.getFavoriteCount()).isEqualTo(1L);
        assertThat(counts.getFollowingCount()).isEqualTo(2L);
        assertThat(counts.getFollowerCount()).isEqualTo(3L);

        verify(authMemberProvider, times(1)).getCurrentMember();
        verify(walkReportFavoriteRepository, times(1)).countByMember(any(Member.class));
        verify(followRepository, times(1)).countByFollower(any(Member.class));
        verify(followRepository, times(1)).countByFollowing(any(Member.class));


    }




}