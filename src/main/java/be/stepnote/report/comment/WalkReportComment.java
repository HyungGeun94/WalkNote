package be.stepnote.report.comment;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import be.stepnote.member.entity.Member;
import be.stepnote.report.walk.entity.WalkReport;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private WalkReportComment parent; // 원본 댓글이면 null

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;



    public static WalkReportComment createReply(Member member, WalkReport report, WalkReportComment parent, String content) {
        return new WalkReportComment(report, member, content, parent);
    }

    public boolean isRoot() {
        return parent == null;
    }


    private WalkReportComment(WalkReport report, Member member, String content, WalkReportComment walkReportComment) {
        this.walkReport = report;
        this.member = member;
        this.content = content;
        this.parent = walkReportComment;
    }

    public static WalkReportComment createRoot(WalkReport report, Member member, String content) {
        return new WalkReportComment(report, member, content, null);
    }
}