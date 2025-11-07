package be.stepnote.alarm;

import be.stepnote.member.entity.Member;
import be.stepnote.report.walk.entity.WalkReport;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
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
    @JoinColumn(name = "report_id")
    private WalkReport report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")  //  알림 받는 사람
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")    //  알림 보낸 사람
    private Member sender;

    private LocalDateTime createdTime = LocalDateTime.now();

    public static Notification createCommentNotification(Member receiver, Member sender, String content, WalkReport report) {
        Notification n = new Notification();
        n.receiver = receiver;
        n.sender = sender;
        n.title = "새 댓글이 달렸어요!";
        n.body = sender.getNickname() + ": " + content;
        n.type = "COMMENT";
        n.report = report;
        return n;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}