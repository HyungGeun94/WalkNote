package be.stepnote.member;

import be.stepnote.global.response.ApiResponse;
import be.stepnote.member.repository.MemberInfoUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/myPage")
    public ApiResponse<MemberInfoResponse> myPage() {

        MemberInfoResponse memberInfoResponse = memberService.userInfo();

        return ApiResponse.success(memberInfoResponse);

    }

    @PatchMapping("/myPage")
    public ApiResponse<Void> myProfileUpdate(@RequestBody MemberInfoUpdateRequest request) {

        memberService.updateUserInfo(request);

        return ApiResponse.success(null);
    }

}
