package be.stepnote.report.favorite;

import static jakarta.persistence.FetchType.LAZY;

import be.stepnote.member.entity.Member;
import be.stepnote.report.walk.entity.WalkReport;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
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
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "walk_report_favorite",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"report_id", "member_id"})
    }
)
public class WalkReportFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private WalkReport walkReport;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime createdAt= LocalDateTime.now();

    public static WalkReportFavorite create(WalkReport walkReport){

        return new WalkReportFavorite(walkReport);

    }

    public void createdBy(Member member){
        this.member = member;
    }


    private WalkReportFavorite(WalkReport walkReport) {
        this.walkReport = walkReport;
    }
}
