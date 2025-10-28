package be.stepnote.report.walk.dto;

import be.stepnote.report.image.WalkReportImage;
import be.stepnote.report.walk.entity.WalkReport;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalkReportDetailResponse {

    private Long id;

    private String nickname;

    private List<String> images;

    private String title;
    private String content;

    private String startPoint;

    private String endPoint;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double distance;
    private long duration;
    private int steps;
    private double calorie;

    private boolean favorite;
    private boolean like;
    private boolean staticHide;


    public WalkReportDetailResponse(WalkReport walkReport) {
        this.id = walkReport.getId();
        this.nickname = walkReport.getCreatedBy().getNickname();
        this.images = walkReport.getImages().stream().map(WalkReportImage::getUrl).collect(
            Collectors.toList());
        this.title = walkReport.getTitle();
        this.content = walkReport.getContent();
        this.startPoint = walkReport.getStartPoint();
        this.endPoint = walkReport.getEndPoint();
        this.startTime = walkReport.getStartTime();
        this.endTime = walkReport.getEndTime();
        this.distance = walkReport.getDistance();
        this.duration = walkReport.getDuration();
        this.steps = walkReport.getSteps();
        this.calorie = walkReport.getCalorie();
        this.staticHide = walkReport.isStaticHide();
    }

    public WalkReportDetailResponse(WalkReport walkReport, Boolean isLike, Boolean isFavorite) {
        this(walkReport);
        like = isLike;
        favorite = isFavorite;
    }
}
