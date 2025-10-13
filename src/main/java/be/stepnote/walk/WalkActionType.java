package be.stepnote.walk;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum WalkActionType {

    START_WALK,        // 산책 시작
    RECORD_LOCATION,   // 산책 중 좌표 기록
    STOP_WALK,         // 산책 종료
    NONE;



//    "walk"나 "WALK"나 "Walk" 전부 정상 매핑
    @JsonCreator
    public static WalkActionType from(String value) {
        return WalkActionType.valueOf(value.toUpperCase());
    }

}

