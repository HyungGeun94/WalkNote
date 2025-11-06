package be.stepnote.report.walk.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import be.stepnote.config.QuerydslConfig;
import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import be.stepnote.report.walk.entity.WalkReport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@DataJpaTest
@Import(QuerydslConfig.class)
class WalkReportCustomRepositoryImplTest {

    @Autowired
    WalkReportCustomRepositoryImpl walkReportCustomRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    WalkReportRepository walkReportRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(Member.create("tester", "테스터"));
    }

    @Test
    void 공개_리포트만_조회된다() {
        // given
        WalkReport publicReport = createWalkReport(member, true);
        WalkReport privateReport = createWalkReport(member, false);
        walkReportRepository.saveAll(List.of(publicReport, privateReport));

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Slice<WalkReport> result =
            walkReportCustomRepository.findReportSummaries(member, pageable, true);

        // then
        assertThat(result.getContent()).containsExactly(publicReport);
        assertThat(result.hasNext()).isFalse();
    }

    // --- 테스트용 리포트 생성 메서드 ---
    private WalkReport createWalkReport(Member member, boolean isPublic) {
        WalkReport report = WalkReport.create(
            3.0, 1000, 50.0,
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now(),
            60,
            "title",
            "content",
            "start",
            "end",
            null
        );
        report.createdBy(member);

        report.upload("title","content",null,isPublic,false);
        return report;
    }
}