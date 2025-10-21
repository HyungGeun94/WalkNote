package be.stepnote.config.fileupload;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private FileService fileService;

    @Test
    @DisplayName("GET /imageUrl/{fileName} 요청 시 Presigned URL 정보가 반환된다")
    @WithMockUser()
    void getPresignedUrl_정상응답() throws Exception {
        // given
        String fileName = "test.png";
        when(fileService.getPresignedUrl(any(), any()))
            .thenReturn(Map.of(
                "url", "https://mock-url.com",
                "fileUrl", "https://bucket.s3.ap-northeast-2.amazonaws.com/images/test.png"
            ));

        // when & then
        mvc.perform(get("/imageUrl/{fileName}", fileName)
                .accept(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.url").value("https://mock-url.com"))
            .andExpect(jsonPath("$.fileUrl").value("https://bucket.s3.ap-northeast-2.amazonaws.com/images/test.png"));
    }
}