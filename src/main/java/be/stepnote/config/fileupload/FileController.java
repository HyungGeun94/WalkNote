package be.stepnote.config.fileupload;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor

public class FileController {

    private final FileService fileService;

    @GetMapping("/imageUrl/{fileName}")
    public Map<String, String> getPresignedUrl(
        @PathVariable(name = "fileName") @Schema(description = "확장자명을 포함해주세요")
        String fileName) {
        return fileService.getPresignedUrl("images", fileName);
    }

}
