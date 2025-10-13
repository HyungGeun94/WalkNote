package be.stepnote.websocket;

import be.stepnote.walk.WalkActionType;
import lombok.Data;

@Data
public class WebSocketRequestDTO {

    private Double latitude; // 위도
    private Double longitude; // 경도
    private WalkActionType actionType;

}
