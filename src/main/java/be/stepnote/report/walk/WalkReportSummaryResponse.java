package be.stepnote.report.walk;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalkReportSummaryResponse {

    private Long id;
    private String title;
    private String image; // 첫 번째 이미지
    private double distance;
    private long duration;
    private int steps;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private String startPoint;
    private String endPoint;


    public WalkReportSummaryResponse(WalkReport walkReport) {
        this.id = walkReport.getId();
        this.title = walkReport.getTitle();
        this.image = walkReport.findFirstImageUrl();
        this.distance = walkReport.getDistance();
        this.duration = walkReport.getDuration();
        this.steps = walkReport.getSteps();
        this.startTime = walkReport.getStartTime();
        this.endTime = walkReport.getEndTime();
        this.createdAt = walkReport.getCreatedAt();
        this.startPoint = walkReport.getStartPoint();
        this.endPoint = walkReport.getEndPoint();
    }
}
