package be.stepnote.report;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@SuppressWarnings("removal")
@WebMvcTest(WalkReportController.class)
@AutoConfigureMockMvc
class WalkReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalkReportService walkReportService;

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void 리포트_생성_반환값확인() throws Exception {

        given(walkReportService.createReport(any())).willReturn(1L);

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
            .andExpect(status().isOk())
            .andExpect(content().string("1"));
    }
}