package be.stepnote.report.like;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkReportLikeRepository extends JpaRepository<WalkReportLike, Long> , WalkReportLikeRepositoryCustom {

}
