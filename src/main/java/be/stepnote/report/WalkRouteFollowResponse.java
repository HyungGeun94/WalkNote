package be.stepnote.report;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WalkRouteFollowResponse {
    private Long reportId;
    private String title;
    private String content;
    private String authorNickname;
    private List<CoordinateResponse> coordinates;

}

