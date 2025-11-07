package be.stepnote.report.block;

import be.stepnote.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blocks")
public class BlockController {

    private final BlockService blockService;

    @Operation(summary = "회원 차단")
    @PostMapping("/{blockedId}")
    public ApiResponse<?> blockMember(@PathVariable Long blockedId) {
        blockService.blockMember(blockedId);
        return ApiResponse.success("차단 완료");
    }

    @Operation(summary = "회원 차단 해제")
    @DeleteMapping("/{blockedId}")
    public ApiResponse<?> unblockMember(@PathVariable Long blockedId) {
        blockService.unblockMember(blockedId);
        return ApiResponse.success("차단 해제 완료");
    }

    @GetMapping("/blocked")
    public ApiResponse<?> getBlockedMembers() {
        return ApiResponse.success(blockService.getBlockedMembers());
    }


}