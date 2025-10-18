package be.stepnote.follow;

import be.stepnote.config.security.CustomOAuth2User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowController {

    private final FollowService followService;

    /**
     * 나를 팔로우한 사람들 (팔로워)
     */
    @GetMapping("/followers")
    public List<MemberSimpleResponse> getFollowers(
        @AuthenticationPrincipal CustomOAuth2User user
    ) {
        return followService.getFollowers(user.getUsername());
    }

    /**
     * 내가 팔로우한 사람들 (팔로잉)
     */
    @GetMapping("/followings")
    public List<MemberSimpleResponse> getFollowings(
        @AuthenticationPrincipal CustomOAuth2User user
    ) {
        return followService.getFollowings(user.getUsername());
    }
}
