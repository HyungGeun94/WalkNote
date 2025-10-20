package be.stepnote.report.walk;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkReportRepository extends JpaRepository<WalkReport, Long>, WalkReportCustomRepository {


}
