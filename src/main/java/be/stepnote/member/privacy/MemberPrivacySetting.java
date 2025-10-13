package be.stepnote.member.privacy;

import be.stepnote.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class MemberPrivacySetting {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private LocationVisibility visibility = LocationVisibility.ALL;  // ALL / FRIENDS_ONLY / NONE

    private boolean showUsers = true; // 다른 사용자 보기
    private boolean showPosts = true; // 발자취 보기

    private LocalDateTime updatedAt;
}