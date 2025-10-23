package be.stepnote.member;

import be.stepnote.member.entity.Member;
import lombok.Data;

@Data
public class MemberInfoResponse {

    private Long id;
    private String nickname;
    private String profileImageUrl;
    private String bio;

    private MemberInfoResponse() {
    }

    private MemberInfoResponse(Long id, String nickname, String profileImageUrl, String bio) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
    }

    public static MemberInfoResponse from(Member member) {
        MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
        memberInfoResponse.setId(member.getId());
        memberInfoResponse.setNickname(member.getNickname());
        memberInfoResponse.setProfileImageUrl(member.getProfileImageUrl());
        memberInfoResponse.setBio(member.getBio());
        return memberInfoResponse;
    }



}
