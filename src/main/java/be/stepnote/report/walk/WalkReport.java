package be.stepnote.report.walk;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

import be.stepnote.member.entity.Member;
import be.stepnote.report.image.WalkReportImage;
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
public class WalkReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double distance;
    private int steps;
    private double calorie;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration;

    private String title;

    @Column(length = 1000)
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    private String startPoint;

    private String endPoint;

    private boolean isPublic;

    private boolean isStaticHide;

    private boolean active = true;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id",updatable = false)
    private Member createdBy;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "walkReport", cascade = ALL, orphanRemoval = true)
    private List<WalkReportImage> images = new ArrayList<>();


    //생성 메서드
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

    public void changeActive() {
        this.active = false;
    }

    public void addImage(WalkReportImage image) {
        image.addWalkReport(this);
        images.add(image);
    }

    public void createdBy(Member createdBy) {
        this.createdBy = createdBy;
    }

    public void update(String title, String content, List<String> imageUrls) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (imageUrls != null) {
            this.images.clear();
            imageUrls.forEach(url -> this.addImage(new WalkReportImage(url)));
        }
    }

    public void upload(String title, String content, List<String> imageUrls,boolean isPublic,boolean isStaticHide) {

        update(title, content, imageUrls);
        this.isPublic = isPublic;
        this.isStaticHide = isStaticHide;


    }


    public String findFirstImageUrl() {
        if (images == null || images.isEmpty()) {
            return null;
        }
        return images.get(0).getUrl();
    }
}