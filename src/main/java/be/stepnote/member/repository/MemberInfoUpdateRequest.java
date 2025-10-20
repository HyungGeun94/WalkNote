package be.stepnote.member.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoUpdateRequest {

    private String nickname;
    private String imageUrl;
    private String bio;

}
