package be.stepnote.report.comment;

import lombok.Getter;

@Getter
public class CommentRequest {
    private Long reportId;  // 댓글 작성 시
    private Long parentId;  // 대댓글 작성 시
    private String content;
}
