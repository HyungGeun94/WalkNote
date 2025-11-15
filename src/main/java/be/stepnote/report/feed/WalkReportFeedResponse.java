package be.stepnote.report.feed;


import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WalkReportFeedResponse {

    private Long reportId;
    private String content;

    // 작성자 정보
    private Long authorId;
    private String authorNickname;
    private String authorProfileImageUrl;

    private List<String> imageUrls;

    private boolean isFollowed;

    private boolean isFavorited;

    private boolean isLiked;
    private Long likeCount;

    private Long commentCount;

    private LocalDateTime createdAt;
}
