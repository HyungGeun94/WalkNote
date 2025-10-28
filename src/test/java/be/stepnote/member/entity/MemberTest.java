package be.stepnote.member.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void createUser_정상_생성된다() {
        // given
        String username = "hyunggeun123";
        String nickname = "형근";

        // when
        Member member = Member.create(username, nickname);

        // then
        assertThat(member.getUsername()).isEqualTo(username);
        assertThat(member.getNickname()).isEqualTo(nickname);
        assertThat(member.getRole()).isEqualTo("ROLE_USER"); // 기본 권한
        assertThat(member.getCreatedAt()).isNotNull();
        assertThat(member.getUpdatedAt()).isNotNull();
    }

    @Test
    void updatePartial_값있는필드만_갱신된다() {
        // given
        Member member = Member.create("user1", "nick1");
        member.updatePartial("기존 bio", "기존닉", "old.png");

        // when
        member.updatePartial("새 bio", "", null); // bio만 바뀌어야 함

        // then
        assertThat(member.getBio()).isEqualTo("새 bio");
        assertThat(member.getNickname()).isEqualTo("기존닉");
        assertThat(member.getProfileImageUrl()).isEqualTo("old.png");
    }

    @Test
    void updateFcmToken_정상갱신() {
        // given
        Member member = Member.create("user2", "nick2");

        // when
        member.updateFcmToken("token_12345");

        // then
        assertThat(member.getFcmToken()).isEqualTo("token_12345");
    }
}