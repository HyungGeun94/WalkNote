package be.stepnote.report;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

import be.stepnote.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class WalkReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double distance;      // 총 거리
    private int steps;
    private double calorie;       // 칼로리
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration;        // 총 시간(분 단위)

    private String title;

    @Column(length = 1000)
    private String content;

    @CreatedBy
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String startPoint;

    private String endPoint;

    private boolean isPublic; // 피드 업로드 유무


    @OneToMany(mappedBy = "walkReport", cascade = ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    private List<WalkReportImage> images = new ArrayList<>();

    public static WalkReport create(WalkReportRequest dto) {
        WalkReport report = new WalkReport();
        report.distance = dto.getDistance();
        report.steps = dto.getSteps();
        report.calorie = dto.getCalorie();
        report.startTime = dto.getStartTime();
        report.endTime = dto.getEndTime();
        report.duration = dto.getDuration();
        report.title = dto.getTitle();
        report.content = dto.getContent();
        report.isPublic = dto.isPublic();
        report.startPoint = dto.getStartPoint();
        report.endPoint = dto.getEndPoint();

        // 이미지 추가
        if (dto.getImages() != null) {
            dto.getImages().forEach(url ->
                report.addImage(new WalkReportImage(url))
            );
        }

        return report;
    }

    public void toggleVisibility() {
        this.isPublic = !this.isPublic;
    }

    public void addImage(WalkReportImage image) {
        image.addWalkReport(this);
        images.add(image);
    }

    public void update(String title, String content, List<String> imageUrls) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (imageUrls != null) {
            this.images.clear();
            imageUrls.forEach(url -> this.addImage(new WalkReportImage(url)));
        }
    }


    public String findFirstImageUrl() {
        if (images == null || images.isEmpty()) {
            return null;
        }
        return images.get(0).getUrl();
    }
}