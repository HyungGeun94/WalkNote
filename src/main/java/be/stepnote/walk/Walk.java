package be.stepnote.walk;

import be.stepnote.member.entity.Member;
import be.stepnote.report.WalkReport;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Walk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk_report_id")
    private WalkReport walkReport;

    @Builder
    public Walk(Member member, LocalDateTime startedAt) {
        this.member = member;
        this.startedAt = startedAt;
    }

    public void changeEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public void changeWalkReport(WalkReport walkReport) {
        this.walkReport = walkReport;
    }

}
