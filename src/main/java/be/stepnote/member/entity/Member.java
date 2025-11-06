package be.stepnote.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 폼 로그인 및 소셜로그인 할 때 고유id에 해당
    @Column(unique = true, nullable = false)
    private String username;

    private String profileImageUrl;

    private String bio;

    // 유저가 설정한 닉네임
    @Column(unique = true, nullable = false)
    private String nickname;

    // 실명
    private String name;

    private String email;

    private String password;

    private String role;

    @Column(length = 255)
    private String fcmToken;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    private boolean isDeleted = false;



//    @Builder(access = AccessLevel.PRIVATE) -> 점진적 도입
    @Builder
    private Member(String username, String role, String nickname,String email,String profileImageUrl, String name,String password) {
        this.username = username;
        this.nickname = nickname;
        this.role = role;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.name = name;
        this.password = password;
    }

    //  내부 전용 팩토리 메서드
    public static Member create(String username, String nickname) {
        return Member.builder()
            .username(username)
            .nickname(nickname)
            .role("ROLE_USER")
            .build();
    }

    public void updateFcmToken(String token) {
        this.fcmToken = token;
    }

    public void updatePartial(String bio, String nickname, String imageUrl) {
        if (hasText(bio)) {
            this.bio = bio;
        }
        if (hasText(nickname)) {
            this.nickname = nickname;
        }
        if (hasText(imageUrl)) {
            this.profileImageUrl = imageUrl;
        }
        this.updatedAt = LocalDateTime.now();
    }

    private boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}