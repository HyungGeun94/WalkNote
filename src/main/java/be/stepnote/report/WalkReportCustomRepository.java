package be.stepnote.report;

import be.stepnote.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface WalkReportCustomRepository {

    Slice<WalkReport> findReportSummaries(Member member,Pageable pageable,boolean publicVisibility);

    Slice<WalkReport> findMyFavorites(Member member, Pageable pageable);



}
