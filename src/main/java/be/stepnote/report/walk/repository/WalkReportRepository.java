package be.stepnote.report.walk.repository;

import be.stepnote.member.entity.Member;
import be.stepnote.report.walk.entity.WalkReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkReportRepository extends JpaRepository<WalkReport, Long>,
    WalkReportCustomRepository {


     Integer countByCreatedBy(Member member);

     Integer countByCreatedByAndIsPublic(Member createdBy, boolean aPublic);
}
