package be.stepnote.report;

import static org.assertj.core.api.Assertions.assertThat;

import be.stepnote.config.security.CustomOAuth2User;
import be.stepnote.config.security.UserDTO;
import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import be.stepnote.report.favorite.WalkReportFavorite;
import be.stepnote.report.favorite.WalkReportFavoriteRepository;
import be.stepnote.report.walk.WalkReport;
import be.stepnote.report.walk.WalkReportRepository;
import be.stepnote.report.walk.WalkReportRequest;
import be.stepnote.report.walk.WalkReportService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class WalkReportServiceTest {

    @Autowired
    private WalkReportService walkReportService;

    @Autowired
    private WalkReportRepository walkReportRepository;

    @Autowired
    private WalkReportFavoriteRepository walkReportFavoriteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {

        Member member = Member.builder()
            .username("testUser22")
            .role("ROLE_USER")
            .build();

        memberRepository.save(member);
        // Auditing(@CreatedBy) 테스트 위해 SecurityContext 설정
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(member.getUsername());
        userDTO.setRole(member.getRole());

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());


        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void 리포트_생성시_기본정보_저장된다() {


        // given
        WalkReportRequest request = new WalkReportRequest(
            3.42, 4875, 128.5,
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now(),
            60,
            "테스트 리포트",
            "날씨가 좋아요",
            "서울",
            "서울",
            false, // isFavorite
            List.of("https://s3.amazonaws.com/img1.png")
        );

        // when
        Long reportId = walkReportService.createReport(request,"testUser22");

        // then
        WalkReport saved = walkReportRepository.findById(reportId).orElseThrow();

        assertThat(saved.getTitle()).isEqualTo("테스트 리포트");
        assertThat(saved.getImages()).hasSize(1);
        assertThat(saved.getCreatedBy().getUsername()).isEqualTo("testUser22");
    }

    @Test
    void 리포트_즐겨찾기_설정시_Favorite_테이블에도_저장된다() {
        // given
        WalkReportRequest request = new WalkReportRequest(
            1.0, 1000, 50.0,
            LocalDateTime.now().minusMinutes(30),
            LocalDateTime.now(),
            30,
            "즐겨찾기 테스트",
            "좋은 코스였음",
            "서울",
            "서울",
            true,  // isFavorite ✅
            List.of()
        );

        // when
        Long reportId = walkReportService.createReport(request,"testUser22");

        // then
        WalkReportFavorite favorite = walkReportFavoriteRepository.findAll().get(0);

        assertThat(favorite.getWalkReport().getId()).isEqualTo(reportId);
        assertThat(favorite.getMember().getUsername()).isEqualTo("testUser22");
    }
}
