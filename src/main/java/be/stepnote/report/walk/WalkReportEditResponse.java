package be.stepnote.report.walk;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WalkReportEditResponse {
    private String title;
    private String content;
    private List<String> imageUrls;
}