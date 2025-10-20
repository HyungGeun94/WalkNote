package be.stepnote.report.walk;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WalkReportRequest {

    private double distance;
    private int steps;
    private double calorie;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration;
    private String title;
    private String content;

    private String startPoint;

    private String endPoint;

    @JsonProperty("isPublic")
    private boolean isPublic;

    @JsonProperty("isFavorite")
    private boolean isFavorite;

    private List<String> images;

}
