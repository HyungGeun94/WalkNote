package be.stepnote.report.like;

import be.stepnote.member.entity.Member;
import java.util.List;
import java.util.Map;

public interface WalkReportLikeRepositoryCustom {

    // 각 리포트별 좋아요 개수
    Map<Long, Long> countLikesByReportIds(List<Long> reportIds);

    // 내가 좋아요한 리포트 ID 리스트
    List<Long> findLikedReportIds(List<Long> reportIds, Member member);

}
