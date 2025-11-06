package be.stepnote.report.walk.controller;

import be.stepnote.global.response.SliceResponse;
import be.stepnote.report.comment.CommentResponse;
import be.stepnote.report.walk.dto.WalkReportDetailResponse;
import be.stepnote.report.walk.dto.WalkReportRequest;
import be.stepnote.report.walk.dto.WalkReportSearchCondition;
import be.stepnote.report.walk.dto.WalkReportSummaryResponse;
import be.stepnote.report.walk.dto.WalkReportUpdateRequest;
import be.stepnote.report.walk.dto.WalkReportUploadRequest;
import be.stepnote.report.walk.service.WalkReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser()
    void 리포트_생성_성공() throws Exception {

        given(walkReportService.createReport(any())).willReturn(1L);

        WalkReportRequest request = new WalkReportRequest(60,100,100, LocalDateTime.now(),LocalDateTime.now(),100,"title","content","startPoint","endPoint",false,
            List.of("image1","imgae2"));
        String requestBody = objectMapper.writeValueAsString(request);


        mockMvc.perform(post("/walk/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .with(csrf())) // Security 켜져 있으니 CSRF도 활성화 필요
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(1));

        verify(walkReportService).createReport(any());
    }

    @Test
    @WithMockUser()
    void 리포트리스트_조회_성공() throws Exception {
        // given
        WalkReportSummaryResponse mockResponse = WalkReportSummaryResponse.builder().id(1L).image("image.png").title("title").steps(100).distance(100).duration(100).isPublic(false).build();
        SliceResponse<WalkReportSummaryResponse> slice =
            new SliceResponse<>(List.of(mockResponse), false, 0, 10, 1);

        given(walkReportService.getReports(any(WalkReportSearchCondition.class)))
            .willReturn(slice);


        // when & then
        mockMvc.perform(get("/walk/report/list")
                .param("page", "0")
                .param("sort", "LATEST")
                .param("publicVisibility", "false"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content[0].title").value("title"))
            .andExpect(jsonPath("$.data.hasNext").value(false))
            .andExpect(jsonPath("$.data.totalCount").value(1));

        verify(walkReportService).getReports(any(WalkReportSearchCondition.class));
    }

    @Test
    @WithMockUser()
    void 리포트_즐겨찾기_조회성공() throws Exception {
        WalkReportSummaryResponse mockResponse = WalkReportSummaryResponse.builder().id(1L).image("image.png").title("title").steps(100).distance(100).duration(100).isPublic(false).build();
        SliceResponse<WalkReportSummaryResponse> slice =
            new SliceResponse<>(List.of(mockResponse), false, 0, 10, 1);

        given(walkReportService.getMyFavoriteReports(any(WalkReportSearchCondition.class)))
            .willReturn(slice);

        // when & then
        mockMvc.perform(get("/walk/report/list/favorites")
                .param("page", "0")
                .param("sort", "LATEST")
                .param("publicVisibility", "false"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content[0].title").value("title"))
            .andExpect(jsonPath("$.data.hasNext").value(false))
            .andExpect(jsonPath("$.data.totalCount").value(1));

        verify(walkReportService).getMyFavoriteReports(any(WalkReportSearchCondition.class));

    }

    @Test
    @WithMockUser()
    void 산책_리포트_조회성공() throws Exception {

        WalkReportDetailResponse walkReportDetailResponse = new WalkReportDetailResponse(1L,
            "nickname", List.of("image1", "image2"), "title", "content", "서울시","서울시",LocalDateTime.now(),
            LocalDateTime.now(),
            100, 100, 100, 100, false, false, false);

        given(walkReportService.reportDetail(any())).willReturn(walkReportDetailResponse);


        mockMvc.perform(get("/walk/report/detail/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.title").value("title"));

        verify(walkReportService).reportDetail(any());


    }

    @Test
    @WithMockUser()
    void 자기_글인지_체크() throws Exception {

        given(walkReportService.authCeheck(any())).willReturn(true);

        mockMvc.perform(get("/walk/report/authCheck/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(true));

        verify(walkReportService).authCeheck(any());

    }

    @Test
    @WithMockUser()
    void 산책_리포트_수정() throws Exception {

        WalkReportUpdateRequest request = new WalkReportUpdateRequest();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);


        mockMvc.perform(patch("/walk/report/edit/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));


        verify(walkReportService).updateReport(any(),any());

    }

    @Test
    @WithMockUser()
    void 산책_수정을위한_리포트_조회성공() throws Exception {

        WalkReportDetailResponse walkReportDetailResponse = new WalkReportDetailResponse(1L,
            "nickname", List.of("image1", "image2"), "title", "content", "서울시","서울시",LocalDateTime.now(),
            LocalDateTime.now(),
            100, 100, 100, 100, false, false, false);

        given(walkReportService.getReportForEdit(any())).willReturn(walkReportDetailResponse);


        mockMvc.perform(get("/walk/report/edit/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.title").value("title"));

        verify(walkReportService).getReportForEdit(any());


    }

    @Test
    @WithMockUser()
    void 리포트_업로드() throws Exception {

        WalkReportUploadRequest request = new WalkReportUploadRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/walk/report/upload/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));


        verify(walkReportService).uploadRort(any(),any());


    }


    @Test
    @WithMockUser()
    void 산책_리포트_삭제() throws Exception {

        mockMvc.perform(delete("/walk/report/1")
            .with(csrf()))
            .andExpect(status().isOk());

        verify(walkReportService).deleteReport(any());
    }


    @Test
    @WithMockUser()
    void 게시글_댓글_조회() throws Exception {

        CommentResponse commentResponse1 = new CommentResponse(1L,"content","nickname",LocalDateTime.now(),0L,"image1");
        CommentResponse commentResponse2 = new CommentResponse(1L,"content","nickname",LocalDateTime.now(),0L,"image2");

        SliceResponse sliceResponse = new SliceResponse(List.of(commentResponse1,commentResponse2), false, 0, 4, 1);

        given(walkReportService.getRootComments(any(),any(Pageable.class))).willReturn(sliceResponse);

        mockMvc.perform(get("/walk/report/reply/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content[0].content").value("content"))
            .andExpect(jsonPath("$.data.content[0].replyCount").value(0))
            .andExpect(jsonPath("$.data.hasNext").value(false))
            .andExpect(jsonPath("$.data.totalCount").value(1));


        verify(walkReportService).getRootComments(any(),any(Pageable.class));
    }



}