package be.stepnote.report.walk.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WalkReportUpdateRequest {
    private String title;
    private String content;
    private List<String> imageUrls; // 새 이미지 리스트
}