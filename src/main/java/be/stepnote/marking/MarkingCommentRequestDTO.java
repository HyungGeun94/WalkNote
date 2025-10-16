package be.stepnote.marking;

import lombok.Data;

@Data
public class MarkingCommentRequestDTO {

    private Double latitude;
    private Double longitude;
    private String comment;

}
