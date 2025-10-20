package be.stepnote.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    폼 로그인 및 소셜로그인 할 때 고유id에 해당
    @Column(unique = true, nullable = true)
    private String username;

    private String profileImageUrl;

    private String bio;

    @Column(unique = true, nullable = true)
    private String nickname;

    private String name;

    private String email;

    private String password;

    private String role;

    private String fcmToken;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public Member(String username, String role, String password,String nickname,String email, String profileImageUrl
    , String bio, String name) {
        this.username = username;
        this.role = role;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.name = name;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
    }

    public void updateFcmToken(String token) {
        this.fcmToken = token;
    }

    public void updatePartial(String bio, String nickname, String imageUrl) {
        if (hasText(bio)) this.bio = bio;
        if (hasText(nickname)) this.nickname = nickname;
        if (hasText(imageUrl)) this.profileImageUrl = imageUrl;
    }

    private boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }
}