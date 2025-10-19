package be.stepnote.report.comment;


import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReplyResponse {

    private Long id;
    private String content;
    private String authorNickname;
    private LocalDateTime createdAt;

    public static ReplyResponse from(WalkReportComment reply) {
        return ReplyResponse.builder()
            .id(reply.getId())
            .content(reply.getContent())
            .authorNickname(reply.getMember().getNickname())
            .createdAt(reply.getCreatedAt())
            .build();
    }
}
