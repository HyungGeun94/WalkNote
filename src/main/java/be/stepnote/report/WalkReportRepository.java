package be.stepnote.report;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkReportRepository extends JpaRepository<WalkReport, Long>, WalkReportCustomRepository {


}
