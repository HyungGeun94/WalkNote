package be.stepnote.follow;

import be.stepnote.config.security.CustomOAuth2User;
import be.stepnote.global.response.ApiResponse;
import be.stepnote.global.response.SliceResponse;
import be.stepnote.report.walk.dto.WalkReportSearchCondition;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ApiResponse<SliceResponse<MemberSimpleResponse>> getFollowers(
        WalkReportSearchCondition condition
    ) {

        SliceResponse<MemberSimpleResponse> followers = followService.getFollowers(condition);
        return ApiResponse.success(followers);
    }

    /**
     * 내가 팔로우한 사람들 (팔로잉)
     */
    @GetMapping("/followings")
    public ApiResponse<SliceResponse<MemberSimpleResponse>> getFollowings(
        WalkReportSearchCondition condition
    ) {
        SliceResponse<MemberSimpleResponse> followings = followService.getFollowings(condition);
        return ApiResponse.success(followings);
    }

    /**
     * 언팔로우
     */
    @DeleteMapping("/unfollow/{userId}")
    public ApiResponse<?> unfollow(@PathVariable Long userId) {

        followService.unfollow(userId);

        return ApiResponse.success(null);


    }

    /**
     * 팔로우
     */
    @PostMapping("/{userId}")
    public ApiResponse<?> follow(@PathVariable Long userId){

        followService.follow(userId);

        return ApiResponse.success(null);


    }
}
