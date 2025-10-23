package be.stepnote.report.favorite;

import be.stepnote.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WalkReportFavoriteRepository extends JpaRepository<WalkReportFavorite, Long> {


    // ✅ 현재 로그인 유저가 즐겨찾기한 리포트 ID 목록
    @Query("select f.walkReport.id from WalkReportFavorite f where f.member.id = :memberId")
    List<Long> findReportIdsByMemberId(@Param("memberId") Long memberId);

    List<WalkReportFavorite> findByMember(Member member);

    Long countByMember(Member member);

}
