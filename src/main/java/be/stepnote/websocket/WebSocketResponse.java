package be.stepnote.websocket;

import be.stepnote.member.dto.MemberNearbyResponse;
import be.stepnote.post.PostNearbyResponse;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebSocketResponse {
    private List<MemberNearbyResponse> users;
    private List<PostNearbyResponse> posts;
}
