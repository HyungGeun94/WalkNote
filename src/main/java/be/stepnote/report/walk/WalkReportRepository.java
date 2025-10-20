package be.stepnote.report.walk;

import be.stepnote.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkReportRepository extends JpaRepository<WalkReport, Long>, WalkReportCustomRepository {


    List<WalkReport> findByCreatedBy(Member member);

    List<WalkReport> findByCreatedByAndIsPublic(Member createdBy, boolean aPublic);
}
