package be.stepnote.alarm;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record NotificationResponse(
    Long id,
    String title,
    String body,
    String type,
    boolean isRead,
    String senderNickname,
    String senderImageUrl,
    LocalDateTime notificationCreatedTime,
    Long reportId
) {
    public static NotificationResponse from(Notification n) {
        return NotificationResponse.builder()
            .id(n.getId())
            .title(n.getTitle())
            .body(n.getBody())
            .type(n.getType())
            .isRead(n.isRead())
            .senderNickname(n.getSender().getNickname())
            .senderImageUrl(n.getSender().getProfileImageUrl())
            .notificationCreatedTime(n.getCreatedTime())
            .reportId(n.getReport() != null ? n.getReport().getId() : null)
            .build();
    }
}
