package be.stepnote.report.walk.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import be.stepnote.member.AuthMemberProvider;
import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import be.stepnote.report.comment.WalkReportCommentRepository;
import be.stepnote.report.favorite.WalkReportFavorite;
import be.stepnote.report.favorite.WalkReportFavoriteRepository;
import be.stepnote.report.feed.WalkReportFeedAssembler;
import be.stepnote.report.like.WalkReportLikeRepository;
import be.stepnote.report.walk.dto.WalkReportRequest;
import be.stepnote.report.walk.entity.WalkReport;
import be.stepnote.report.walk.repository.WalkReportRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WalkReportServiceTest {

    @InjectMocks
    WalkReportService walkReportService;

    @Mock
    WalkReportRepository walkReportRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    WalkReportFavoriteRepository walkReportFavoriteRepository;
    @Mock
    WalkReportFeedAssembler feedAssembler;
    @Mock
    WalkReportCommentRepository commentRepository;
    @Mock
    AuthMemberProvider authMemberProvider;
    @Mock
    WalkReportLikeRepository walkReportLikeRepository;



    @Test
    void 리포트_생성_성공_favorite_false() {
        // given
        Member mockMember = Member.create("username", "nickname");
        given(authMemberProvider.getCurrentMember()).willReturn(mockMember);

        WalkReportRequest dto = new WalkReportRequest(
            1.2, 3000, 50,
            LocalDateTime.now(), LocalDateTime.now().plusMinutes(30),
            1800, "title", "content", "start", "end", false, List.of("img1")
        );

        // when
        walkReportService.createReport(dto);

        // then
        verify(authMemberProvider).getCurrentMember();
        verify(walkReportRepository).save(any(WalkReport.class));
        verify(walkReportFavoriteRepository, never()).save(any());
    }

    @Test
    void 리포트_생성_성공_favorite_true() {
        // given
        Member mockMember = Member.create("username", "nickname");
        given(authMemberProvider.getCurrentMember()).willReturn(mockMember);

        WalkReportRequest dto = new WalkReportRequest(
            1.2, 3000, 50,
            LocalDateTime.now(), LocalDateTime.now().plusMinutes(30),
            1800, "title", "content", "start", "end", true, List.of("img1")
        );

        // when
        walkReportService.createReport(dto);

        // then
        verify(walkReportRepository).save(any(WalkReport.class));
        verify(walkReportFavoriteRepository).save(any(WalkReportFavorite.class));
    }

    @Test
    void 리포트_생성시_작동_검증() {
        // given
        Member mockMember = Member.create("username", "nickname");
        WalkReport mockReport = mock(WalkReport.class);
        given(authMemberProvider.getCurrentMember()).willReturn(mockMember);

        WalkReportRequest dto = new WalkReportRequest(
            1.2, 3000, 50,
            LocalDateTime.now(), LocalDateTime.now().plusMinutes(30),
            1800, "title", "content", "start", "end", false, List.of("img1")
        );

        // when
        Long result = walkReportService.createReport(dto);
//
//        // then
        verify(walkReportRepository).save(any(WalkReport.class));
    }




}