package be.stepnote.alarm;

import be.stepnote.member.entity.Member;
import be.stepnote.member.entity.NotificationSetting;
import be.stepnote.member.repository.MemberRepository;
import be.stepnote.member.repository.NotificationSettingRepository;
import be.stepnote.report.walk.entity.WalkReport;
import be.stepnote.report.walk.repository.WalkReportRepository;
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
    private final WalkReportRepository walkReportRepository;

    @Async
    @Transactional
    public void handleCommentNotification(Long reportId, Long writerId, String content) {

        WalkReport report = walkReportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("리포트 없음"));

        Member target = report.getCreatedBy();

        NotificationSetting setting = notificationSettingRepository.findByMember(target)
            .orElseThrow(() -> new IllegalStateException("알림 설정 없음"));

        Notification notification = Notification.createCommentNotification(
            target,
            memberRepository.findById(writerId).orElseThrow(),
            content,
            report
        );
        notificationRepository.save(notification);

        if (setting.isCommentEnabled()) {
            firebaseService.send(
                "새 댓글이 달렸어요!",
                notification.getBody(),
                target.getFcmToken()
            );
        }
    }
}