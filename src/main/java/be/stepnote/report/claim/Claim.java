package be.stepnote.report.claim;

import be.stepnote.member.entity.Member;
import be.stepnote.report.walk.entity.WalkReport;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이의 제기 주체 (나)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claimer_id", nullable = false)
    private Member claimer;

    // 대상 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walk_report_id", nullable = false)
    private WalkReport walkReport;

    private String category; // 예: SPAM, OFFENSIVE, HARASSMENT, ETC
    private String detail;   // 구체적 사유
    private LocalDateTime createdAt;

    public static Claim create(Member claimer, WalkReport walkReport, String category, String detail) {
        Claim claim = new Claim();
        claim.claimer = claimer;
        claim.walkReport = walkReport;
        claim.category = category;
        claim.detail = detail;
        claim.createdAt = LocalDateTime.now();
        return claim;
    }
}
