package be.stepnote.config.fileupload;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.net.URL;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private AmazonS3 amazonS3;  // 외부 의존성은 Mock

    @InjectMocks
    private FileService fileService; // 실제 로직만 테스트 대상

    @Test
    void presignedUrl_생성_로직_검증() {

        // given
        ReflectionTestUtils.setField(fileService, "bucket", "test-bucket");
        ReflectionTestUtils.setField(fileService, "region", "ap-northeast-2");

        String prefix = "images";
        String fileName = "test.png";

        URL mockUrl = toUrl("https://mock-url.com");
        when(amazonS3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class)))
            .thenReturn(mockUrl);

        // when
        Map<String, String> result = fileService.getPresignedUrl(prefix, fileName);

        // then
        assertThat(result.get("url")).isEqualTo("https://mock-url.com");
        assertThat(result.get("fileUrl")).contains("test-bucket");
        assertThat(result.get("fileUrl")).contains("images/");
        assertThat(result.get("fileUrl")).endsWith(".png");
    }

    private URL toUrl(String url) {
        try {
            return new URL(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}