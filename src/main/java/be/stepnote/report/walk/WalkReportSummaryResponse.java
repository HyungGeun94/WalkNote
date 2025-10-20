package be.stepnote.report.walk;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class WalkReportSummaryResponse {

    private Long id;
    private String title;
    private String image; // 첫 번째 이미지
    private double distance;
    private long duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;

    public WalkReportSummaryResponse(WalkReport walkReport) {
        this.id = walkReport.getId();
        this.title = walkReport.getTitle();
        this.image = walkReport.findFirstImageUrl();
        this.distance = walkReport.getDistance();
        this.duration = walkReport.getDuration();
        this.startTime = walkReport.getStartTime();
        this.endTime = walkReport.getEndTime();
        this.createdAt = walkReport.getCreatedAt();
    }
}
