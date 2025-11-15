package be.stepnote.report.feed;

import be.stepnote.follow.FollowRepository;
import be.stepnote.member.entity.Member;
import be.stepnote.report.image.WalkReportImage;
import be.stepnote.report.walk.entity.WalkReport;
import be.stepnote.report.favorite.WalkReportFavoriteRepository;
import be.stepnote.report.comment.WalkReportCommentRepository;
import be.stepnote.report.like.WalkReportLikeRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalkReportFeedAssembler {

    private final FollowRepository followRepository;

    private final WalkReportFavoriteRepository favoriteRepository;

    private final WalkReportLikeRepository likeRepository;

    private final WalkReportCommentRepository commentRepository;



    public List<WalkReportFeedResponse> assemble(List<WalkReport> reports, Member me) {


        // 1️⃣ 내가 팔로우한 사람들의 ID
        List<Long> followingId = followRepository.findFollowingIdsByFollowerId(me.getId());

        // 2️⃣ 내가 즐겨찾기한 리포트 ID 목록
        List<Long> favoritedIds = favoriteRepository.findReportIdsByMemberId(me.getId());


        Set<Long> likedSet = new HashSet<>(likeRepository.findLikedReportIds(reports.stream().map(WalkReport::getId).collect(Collectors.toList()), me));

        Map<Long, Long> likeCountMap = likeRepository.countLikesByReportIds(reports.stream().map(WalkReport::getId).collect(Collectors.toList()));

        Map<Long, Long> commentCountMap = commentRepository.countCommentsByReportIds(
            reports.stream().map(WalkReport::getId).toList()
        );




        return reports.stream()
            .map(report -> WalkReportFeedResponse
                .builder()
                .reportId(report.getId())
                .authorId(report.getCreatedBy().getId())
                .content(report.getContent())
                .authorNickname(report.getCreatedBy().getNickname())
                .authorProfileImageUrl(report.getCreatedBy().getProfileImageUrl())
                .createdAt(report.getCreatedAt())
                .imageUrls(report.getImages().stream().map(WalkReportImage::getUrl).toList())
                .isFollowed(followingId.contains(report.getCreatedBy().getId()))
                .isFavorited(favoritedIds.contains(report.getId())) //  즐겨찾기 여부 체크
                .isLiked(likedSet.contains(report.getId())) // 내가 좋아요한 게시물인지?
                .likeCount(likeCountMap.getOrDefault(report.getId(),0L))
                .commentCount(commentCountMap.getOrDefault(report.getId(),0L))
                .build())
                .collect(Collectors.toList());


    }
}
