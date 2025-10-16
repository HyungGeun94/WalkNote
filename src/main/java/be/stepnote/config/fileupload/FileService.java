package be.stepnote.config.fileupload;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final AmazonS3 amazonS3;

    public Map<String, String> getPresignedUrl(String prefix, String fileName) {
        if (!prefix.isEmpty()) {
            fileName = createPath(prefix, fileName);
        }

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, fileName)
            .withMethod(HttpMethod.PUT)
            .withExpiration(getPresignedUrlExpiration());

        URL url = amazonS3.generatePresignedUrl(request);

        String fileUrl = String.format(
            "https://%s.s3.%s.amazonaws.com/%s",
            bucket,
            region,
            fileName
        );
        return Map.of(
            "url", url.toString(),
            "fileUrl", fileUrl
        );
    }

    private Date getPresignedUrlExpiration() {
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + 1000 * 60 * 5); // 5분 유효
        return expiration;
    }

    private String createPath(String prefix, String fileName) {
        String fileId = UUID.randomUUID().toString();
        return String.format("%s/%s", prefix, fileId + "-" + fileName);
    }
}

