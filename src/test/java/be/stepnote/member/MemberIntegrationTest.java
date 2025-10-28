package be.stepnote.member;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberInfoUpdateRequest;
import be.stepnote.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AuthMemberProvider authMemberProvider;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(Member.create("hyunggeun", "형근"));
    }

    @Test
    @WithMockUser(username = "noExist")
    void 회원탈퇴나존재하지_않는_유저() throws Exception {

        assertThatThrownBy(() -> authMemberProvider.getMember("noExist"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("사용자 없음");

    }


    @Test
    @WithMockUser(username = "hyunggeun")
    void myPage_통합_조회성공() throws Exception {

        // when & then
        mockMvc.perform(get("/myPage"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.nickname").value("형근"));
    }

    @Test
    @WithMockUser(username = "hyunggeun")
    void count_조회() throws Exception {

        mockMvc.perform(get("/myPage/count"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.favoriteCount").value(0))
            .andExpect(jsonPath("$.data.followerCount").value(0))
            .andExpect(jsonPath("$.data.followingCount").value(0));

    }

    @Test
    @WithMockUser(username = "hyunggeun")
    void userUpdate() throws Exception {

        MemberInfoUpdateRequest request = new MemberInfoUpdateRequest("바뀐닉네임", "image.url", null);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/myPage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());

        Member findMember = memberRepository.findById(member.getId()).get();

        assertThat(findMember.getNickname()).isEqualTo("바뀐닉네임");
        assertThat(findMember.getProfileImageUrl()).isEqualTo("image.url");
        assertThat(findMember.getBio()).isNull();


    }

}
