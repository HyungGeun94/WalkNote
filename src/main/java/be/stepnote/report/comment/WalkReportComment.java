package be.stepnote.report.comment;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import be.stepnote.member.entity.Member;
import be.stepnote.report.WalkReport;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "walk_report_comment")
public class WalkReportComment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "walk_report_id", nullable = false)
    private WalkReport walkReport;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    // 대댓글 대비용 (nullable)
    @Column(name = "parent_id")
    private Long parentId;

    private WalkReportComment(WalkReport report, Member member, String content, Long parentId) {
        this.walkReport = report;
        this.member = member;
        this.content = content;
        this.parentId = parentId;
    }

    public static WalkReportComment create(WalkReport report, Member member, String content) {
        return new WalkReportComment(report, member, content, null);
    }
}