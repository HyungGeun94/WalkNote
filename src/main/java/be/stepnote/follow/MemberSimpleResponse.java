package be.stepnote.follow;

import be.stepnote.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSimpleResponse {

    private Long memberId;          // 회원 ID
    private String nickname;        // 닉네임
    private String profileImageUrl; // 프로필 이미지 (nullable)
    private String bio;             // 소개글 (nullable)

    public static MemberSimpleResponse from(Member member) {
        return new MemberSimpleResponse(
            member.getId(),
            member.getNickname(),
            member.getProfileImageUrl(),
            member.getBio()
        );
    }
}
