package be.stepnote.marking;

import be.stepnote.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkingCommentResponseDTO {

    private Long id;
    private String comment;
    private double latitude;
    private double longitude;
    private String writerNickname;
    private String writerProfileUrl;

    public static MarkingCommentResponseDTO from(MarkingComment comment) {
        Member writer = comment.getCreatedBy();

        return new MarkingCommentResponseDTO(
            comment.getId(),
            comment.getComment(),
            comment.getLatitude(),
            comment.getLongitude(),
            writer != null ? writer.getNickname() : "익명",
            writer != null ? writer.getProfileImageUrl() : null
        );
    }
}
