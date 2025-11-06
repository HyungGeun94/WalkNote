package be.stepnote.alarm;

import be.stepnote.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;
    private String type; // ex) COMMENT, LIKE, FOLLOW
    private boolean isRead = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  // 알림 받을 사람

    public static Notification createCommentNotification(Member member, Member writer, String content, Long reportId) {
        Notification n = new Notification();
        n.member = member;
        n.title = "새 댓글이 달렸어요!";
        n.body = writer.getNickname() + ": " + content;
        n.type = "COMMENT";
        return n;
    }
}