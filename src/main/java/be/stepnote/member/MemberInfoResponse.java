package be.stepnote.member;

import be.stepnote.member.entity.Member;
import lombok.Data;

@Data
public class MemberInfoResponse {

    Long id;
    String usernickname;
    String profileImageUrl;
    String bio;

    private MemberInfoResponse() {
    }

    private MemberInfoResponse(Long id, String usernickname, String profileImageUrl, String bio) {
        this.id = id;
        this.usernickname = usernickname;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
    }

    public static MemberInfoResponse from(Member member) {
        MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
        memberInfoResponse.setId(member.getId());
        memberInfoResponse.setUsernickname(member.getNickname());
        memberInfoResponse.setProfileImageUrl(member.getProfileImageUrl());
        memberInfoResponse.setBio(member.getBio());
        return memberInfoResponse;
    }



}
