package be.stepnote.report.comment;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentResponse {

    private Long id;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private Long replyCount;
    private String profileImageUrl;

    public static CommentResponse from(WalkReportComment comment, long replyCount) {
        return CommentResponse.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .nickname(comment.getMember().getNickname())
            .profileImageUrl(comment.getMember().getProfileImageUrl())
            .createdAt(comment.getCreatedAt())
            .replyCount(replyCount)
            .build();
    }
}
