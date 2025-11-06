package be.stepnote.report.favorite;

import be.stepnote.member.entity.Member;
import be.stepnote.report.walk.entity.WalkReport;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WalkReportFavoriteRepository extends JpaRepository<WalkReportFavorite, Long> {


    // ✅ 현재 로그인 유저가 즐겨찾기한 리포트 ID 목록
    @Query("select f.walkReport.id from WalkReportFavorite f where f.member.id = :memberId")
    List<Long> findReportIdsByMemberId(@Param("memberId") Long memberId);

    Long countByMember(Member member);

    Optional<Boolean> existsByWalkReportAndMember(WalkReport walkReport, Member member);

    Slice<WalkReportFavorite> findByMember(Member member, Pageable pageable);

    void deleteByMemberAndWalkReportIdIn(Member member, List<Long> ids);
}
