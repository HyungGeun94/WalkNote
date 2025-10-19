package be.stepnote.report.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkReportCommentRepository extends JpaRepository<WalkReportComment, Long> ,WalkReportCommentRepositoryCustom {

}
