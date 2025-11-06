package be.stepnote.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettingRequest {

    private boolean likeEnabled;
    private boolean commentEnabled;
    private boolean saveEnabled;
    private boolean followEnabled;

    // 전체 허용 / 전체 거부를 한 번에 처리할 수 있게 하는 팩토리 메서드
    public static NotificationSettingRequest all(boolean enabled) {
        return new NotificationSettingRequest(enabled, enabled, enabled, enabled);
    }
}