package be.stepnote.report.like;

import be.stepnote.member.entity.Member;
import be.stepnote.report.walk.WalkReport;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkReportLikeRepository extends JpaRepository<WalkReportLike, Long> , WalkReportLikeRepositoryCustom {

    Optional<Boolean> existsByWalkReportAndMember(WalkReport walkReport, Member member);
}
