package be.stepnote.report;

import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WalkReportService {

    private final WalkReportRepository walkReportRepository;
    private final MemberRepository memberRepository;
    private final WalkReportFavoriteRepository walkReportFavoriteRepository;

    public Long createReport(WalkReportRequest dto) {

        WalkReport walkReport = WalkReport.create(dto);

        WalkReport saved = walkReportRepository.save(walkReport);


        if(dto.isFavorite()){
            WalkReportFavorite walkReportFavorite = WalkReportFavorite.create(walkReport);
            walkReportFavoriteRepository.save(walkReportFavorite);
        }

        return saved.getId();
    }

    @Transactional(readOnly = true)
    public Slice<WalkReportSummaryResponse> getReports(Pageable pageable,String username,boolean publicVisibility) {
        Member member = memberRepository.findByUsername(username).orElseThrow();

        Slice<WalkReport> walkReports = walkReportRepository.findReportSummaries(member,
            pageable,publicVisibility);


        return walkReports.map(walkReport -> new WalkReportSummaryResponse(walkReport));
    }

    public Slice<WalkReportSummaryResponse> getMyFavoriteReports(Pageable pageable, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow();

        Slice<WalkReport> favorites = walkReportRepository.findMyFavorites(member, pageable);

        return favorites.map(WalkReportSummaryResponse::new);
    }

}
