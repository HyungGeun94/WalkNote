package be.stepnote.report.comment;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {

    private Long id;
    private String content;
    private String authorNickname;
    private LocalDateTime createdAt;
    private Long replyCount;

    public static CommentResponse from(WalkReportComment comment, long replyCount) {
        return CommentResponse.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .authorNickname(comment.getMember().getNickname())
            .createdAt(comment.getCreatedAt())
            .replyCount(replyCount)
            .build();
    }
}
