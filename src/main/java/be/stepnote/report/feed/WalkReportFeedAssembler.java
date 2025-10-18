package be.stepnote.report.feed;

import be.stepnote.follow.FollowRepository;
import be.stepnote.member.entity.Member;
import be.stepnote.report.WalkReport;
import be.stepnote.report.WalkReportFavoriteRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalkReportFeedAssembler {

    private final FollowRepository followRepository;

    private final WalkReportFavoriteRepository favoriteRepository;



    public List<WalkReportFeedResponse> assemble(List<WalkReport> reports, Member me) {


        // 1️⃣ 내가 팔로우한 사람들의 ID
        List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(me.getId());

        // 2️⃣ 내가 즐겨찾기한 리포트 ID 목록
        List<Long> favoritedIds = favoriteRepository.findReportIdsByMemberId(me.getId());


        return reports.stream()
            .map(report -> WalkReportFeedResponse.builder()
                .reportId(report.getId())
                .content(report.getContent())
                .authorId(report.getCreatedBy().getId())
                .authorNickname(report.getCreatedBy().getNickname())
                .authorProfileImageUrl(report.getCreatedBy().getProfileImageUrl())
                .imageUrls(report.getImages().stream()
            .map(img -> img.getUrl())
            .toList())
                // 2️⃣ 현재 report 작성자가 내가 팔로우한 사람인지 체크
                .isFollowed(followingIds.contains(report.getCreatedBy().getId()))
                .isFavorited(favoritedIds.contains(report.getId())) // ✅ 즐겨찾기 여부 체크
                .createdAt(report.getCreatedAt())
                .build())
            .collect(Collectors.toList());


    }
}
