package be.stepnote.report;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import be.stepnote.config.security.CustomOAuth2User;
import be.stepnote.config.security.UserDTO;
import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import be.stepnote.report.walk.WalkReportController;
import be.stepnote.report.walk.WalkReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(WalkReportController.class)
@AutoConfigureMockMvc
@Import(MemberRepository.class)
class WalkReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalkReportService walkReportService;

    @MockBean
    private MemberRepository memberRepository; // ✅ 추가




    @BeforeEach
    void setUp() {

        Member member = Member.builder()
            .username("testUser22")
            .role("ROLE_USER")
            .build();

        given(memberRepository.save(any())).willReturn(member); // ✅ Mock 동작 정의

        // Auditing(@CreatedBy) 테스트 위해 SecurityContext 설정
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(member.getUsername());
        userDTO.setRole(member.getRole());

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());


        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void 리포트_생성_반환값확인() throws Exception {

        given(walkReportService.createReport(any(),any())).willReturn(1L);

        mockMvc.perform(post("/walk/report")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                      "distance": 3.42,
                      "steps": 4875,
                      "calorie": 128.5,
                      "isPublic": true,
                      "isFavorite": true
                    }
                """))
            .andExpect(jsonPath("$.data").value(1))
            .andExpect(jsonPath("$.success").value(true));
    }
}