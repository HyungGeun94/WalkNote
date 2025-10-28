package be.stepnote.report.walk;

import be.stepnote.report.walk.controller.WalkReportController;
import be.stepnote.report.walk.service.WalkReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalkReportController.class)
@AutoConfigureMockMvc
class WalkReportControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WalkReportService walkReportService;

    @Test
    @WithMockUser()
    void 리포트_생성_성공() throws Exception {

        given(walkReportService.createReport(any())).willReturn(1L);

        mockMvc.perform(post("/walk/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "distance": 3.42,
                      "steps": 4875,
                      "calorie": 128.5,
                      "isPublic": true,
                      "isFavorite": true
                    }
                """)
                .with(csrf())) // Security 켜져 있으니 CSRF도 활성화 필요
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(1));

        verify(walkReportService).createReport(any());
    }
}