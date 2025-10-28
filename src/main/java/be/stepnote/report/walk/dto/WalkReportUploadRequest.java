package be.stepnote.report.walk.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WalkReportUploadRequest {
    private String title;
    private String content;
    private List<String> imageUrls;
    private boolean publicStatus;
    private boolean staticHideStatus;
}