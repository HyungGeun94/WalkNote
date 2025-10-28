package be.stepnote.report.comment;

import be.stepnote.report.walk.entity.WalkReport;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkReportCommentRepository extends JpaRepository<WalkReportComment, Long> ,WalkReportCommentRepositoryCustom {



    // 특정 원본 댓글의 대댓글 조회
    List<WalkReportComment> findByParentOrderByCreatedAtAsc(WalkReportComment parent);

    Slice<WalkReportComment> findByWalkReportAndParentIsNull(
        WalkReport report,
        Pageable pageable
    );

    // 대댓글 개수
    long countByParent(WalkReportComment parent);

}
