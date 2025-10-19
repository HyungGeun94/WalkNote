package be.stepnote.report.comment;

import be.stepnote.report.WalkReport;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkReportCommentRepository extends JpaRepository<WalkReportComment, Long> ,WalkReportCommentRepositoryCustom {

    // 원본 댓글 조회 (최신순)
    List<WalkReportComment> findByWalkReportAndParentIsNullOrderByCreatedAtDesc(WalkReport report);

    // 특정 원본 댓글의 대댓글 조회
    List<WalkReportComment> findByParentOrderByCreatedAtAsc(WalkReportComment parent);

    // 원본 댓글 (parent=null)
    List<WalkReportComment> findByWalkReportAndParentIsNullOrderByCreatedAtDesc(WalkReport report, Pageable pageable);

    // 대댓글 개수
    long countByParent(WalkReportComment parent);

}
