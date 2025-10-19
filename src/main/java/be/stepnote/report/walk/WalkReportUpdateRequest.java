package be.stepnote.report.walk;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WalkReportUpdateRequest {
    private String title;
    private String content;
    private List<String> imageUrls; // 새 이미지 리스트
}