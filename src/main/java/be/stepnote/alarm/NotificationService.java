package be.stepnote.alarm;

import be.stepnote.member.entity.Member;
import be.stepnote.member.entity.NotificationSetting;
import be.stepnote.member.repository.MemberRepository;
import be.stepnote.member.repository.NotificationSettingRepository;
import be.stepnote.report.walk.entity.WalkReport;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FirebaseService firebaseService;
    private final NotificationSettingRepository notificationSettingRepository;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @Async
    @Transactional
    public void handleCommentNotification(WalkReport report, Member writer, String content) {

        Member target = report.getCreatedBy();

        Notification notification = Notification.createCommentNotification(
            target, writer, content, report.getId()
        );
        notificationRepository.save(notification);

        NotificationSetting setting = notificationSettingRepository.findByMember(target)
            .orElseThrow(() -> new IllegalStateException("알림 설정 없음"));

        if (!setting.isCommentEnabled()) return; // 알림 비활성화 시 바로 종료

        firebaseService.send(
            "새 댓글이 달렸어요!",
            writer.getNickname() + ": " + content,
            target.getFcmToken()
        );
    }
}