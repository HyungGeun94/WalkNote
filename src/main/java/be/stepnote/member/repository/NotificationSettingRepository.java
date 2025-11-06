package be.stepnote.member.repository;

import be.stepnote.member.entity.Member;
import be.stepnote.member.entity.NotificationSetting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationSettingRepository extends JpaRepository<NotificationSetting, Long> {

    Optional<NotificationSetting> findByMember(Member member);
}
