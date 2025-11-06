package be.stepnote.member.dto;

import be.stepnote.member.entity.NotificationSetting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettingResponse {

    private boolean likeEnabled;
    private boolean commentEnabled;
    private boolean saveEnabled;
    private boolean followEnabled;

    public static NotificationSettingResponse from(NotificationSetting setting) {
        return new NotificationSettingResponse(
            setting.isLikeEnabled(),
            setting.isCommentEnabled(),
            setting.isSaveEnabled(),
            setting.isFollowEnabled()
        );
    }
}