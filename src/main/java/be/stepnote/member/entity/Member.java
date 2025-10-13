package be.stepnote.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    폼 로그인할 때 아이디에 해당.
    private String username;

    @Column(unique = true, nullable = false)
    private String nickname;

    private String name;

    private String password;

    private String role;

    @Builder
    public Member(String username, String role, String password,String nickname) {
        this.username = username;
        this.role = role;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
    }
}