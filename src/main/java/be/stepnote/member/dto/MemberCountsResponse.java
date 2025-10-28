package be.stepnote.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberCountsResponse {
    private long favoriteCount;
    private long followerCount;
    private long followingCount;
}
