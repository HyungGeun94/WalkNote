package be.stepnote.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import be.stepnote.member.dto.MemberCountsResponse;
import be.stepnote.member.dto.MemberInfoResponse;
import be.stepnote.member.repository.MemberInfoUpdateRequest;
import be.stepnote.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @MockBean
    MemberService memberService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser
    void myPage() throws Exception {
        //given
        given(memberService.userInfo()).willReturn(new MemberInfoResponse(1L,"hyunggeun","hyunggeun.img","방가방가"));

        //when && then
        mockMvc.perform(get("/myPage"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.id").value(1L))
            .andExpect(jsonPath("$.data.nickname").value("hyunggeun"))
            .andExpect(jsonPath("$.data.profileImageUrl").value("hyunggeun.img"))
            .andExpect(jsonPath("$.data.bio").value("방가방가"));


        verify(memberService, times(1)).userInfo(); // 정확히 한 번 호출됨

    }

    @Test
    @WithMockUser
    void getmyCount() throws Exception {
        //given
        given(memberService.getCounts()).willReturn(new MemberCountsResponse(1L,2L,3L));


        //when & then
        mockMvc.perform(get("/myPage/count"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.favoriteCount").value(1L))
            .andExpect(jsonPath("$.data.followerCount").value(2L))
            .andExpect(jsonPath("$.data.followingCount").value(3L));

        verify(memberService, times(1)).getCounts();

    }

    @Test
    @WithMockUser
    void myProfileUpdate() throws Exception {
        //given
        MemberInfoUpdateRequest request = new MemberInfoUpdateRequest("nickname","url","bio");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);

        //when & then
        mockMvc.perform(patch("/myPage")
                .with(csrf()) //
                .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk());


        verify(memberService, times(1)).updateUserInfo(any());

    }
}