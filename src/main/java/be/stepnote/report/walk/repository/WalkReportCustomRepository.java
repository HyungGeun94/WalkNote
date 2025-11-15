package be.stepnote.report.walk.repository;

import be.stepnote.member.entity.Member;
import be.stepnote.report.walk.dto.WalkReportSearchCondition;
import be.stepnote.report.walk.entity.WalkReport;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface WalkReportCustomRepository {

    Slice<WalkReport> findReportSummaries(Member member,Pageable pageable,boolean publicVisibility);

    Slice<WalkReport> findMyFavorites(Member member, Pageable pageable);

    Slice<WalkReport> getFeedReports(
            WalkReportSearchCondition condition,
            List<Long> blockedIds
        );



}
