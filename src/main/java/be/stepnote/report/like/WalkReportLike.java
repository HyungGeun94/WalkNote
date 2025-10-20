package be.stepnote.report.like;

import static jakarta.persistence.FetchType.*;

import be.stepnote.member.entity.Member;
import be.stepnote.report.walk.WalkReport;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(
    name = "walk_report_like",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"walk_report_id", "member_id"}
        )
    }
)
public class WalkReportLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "walk_report_id", nullable = false)
    private WalkReport walkReport;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @CreatedDate
    private LocalDateTime createdAt;

    private WalkReportLike(WalkReport walkReport, Member member) {
        this.walkReport = walkReport;
        this.member = member;
    }

    public static WalkReportLike create(WalkReport report, Member member) {
        return new WalkReportLike(report, member);
    }
}
