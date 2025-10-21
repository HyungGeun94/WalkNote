package be.stepnote.config.fileupload;


import static org.assertj.core.api.Assertions.*;

import com.amazonaws.services.s3.AmazonS3;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class S3ConfigTest {

    @Autowired
    private AmazonS3 amazonS3;

    @Test
    void S3_빈_정상_등록_확인() {
        assertThat(amazonS3).isNotNull();
    }
}