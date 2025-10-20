package be.stepnote.report.walk;

import be.stepnote.config.security.CustomOAuth2User;
import be.stepnote.member.entity.Member;
import be.stepnote.report.WalkRouteFollowResponse;
import be.stepnote.report.comment.CommentRequest;
import be.stepnote.report.comment.CommentResponse;
import be.stepnote.report.comment.ReplyResponse;
import be.stepnote.report.feed.WalkReportFeedResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/walk/report")
@RequiredArgsConstructor
@Slf4j
public class WalkReportController {

    private final int DEFAULT_PAGE_SIZE = 2;

    private final WalkReportService walkReportService;


    @PostMapping
    public ResponseEntity<Long> createReport(@RequestBody WalkReportRequest request
    ) {
        Long reportId = walkReportService.createReport(request);

        return ResponseEntity.ok(reportId);
    }


    @GetMapping("/list")
    public ResponseEntity<Slice<WalkReportSummaryResponse>> getReports(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "LATEST") String sort,
        @AuthenticationPrincipal CustomOAuth2User user,
        @RequestParam(defaultValue = "false") boolean publicVisibility
    ) {

        Sort sortOption = sort.equalsIgnoreCase("OLDEST")
            ? Sort.by(Sort.Direction.ASC, "createdAt")
            : Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE, sortOption);

        Slice<WalkReportSummaryResponse> response =
            walkReportService.getReports(pageable, user.getUsername(), publicVisibility);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/list/favorites")
    public ResponseEntity<Slice<WalkReportSummaryResponse>> getMyFavorites(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "LATEST") String sort,
        @AuthenticationPrincipal CustomOAuth2User user
        ) {
        Sort sortOption = sort.equalsIgnoreCase("OLDEST")
            ? Sort.by(Sort.Direction.ASC, "createdAt")
            : Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE, sortOption);

        Slice<WalkReportSummaryResponse> response =
            walkReportService.getMyFavoriteReports(pageable, user.getUsername());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/feed")
    public List<WalkReportFeedResponse> getFeed(
        @AuthenticationPrincipal CustomOAuth2User user,
        Pageable pageable
    ) {
        return walkReportService.getFeed(pageable, user.getUsername());
    }

    @PostMapping("/api/reports/{reportId}/public")
    public ResponseEntity<Void> toggleVisibility(
        @AuthenticationPrincipal CustomOAuth2User user,
        @PathVariable Long reportId
    ) {
        walkReportService.toggleVisibility(user.getUsername(), reportId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/api/reports/{id}")
    public ResponseEntity<Void> updateReport(
        @AuthenticationPrincipal CustomOAuth2User me,
        @PathVariable Long id,
        @RequestBody WalkReportUpdateRequest request
    ) {
        walkReportService.updateReport(me.getUsername(), id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/reports/{id}")
    public ResponseEntity<WalkReportEditResponse> getReportForEdit(
        @AuthenticationPrincipal CustomOAuth2User me,
        @PathVariable Long id
    ) {
        WalkReportEditResponse response = walkReportService.getReportForEdit(me.getUsername(), id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/reports/{id}/follow")
    public ResponseEntity<WalkRouteFollowResponse> getRouteForFollow(@PathVariable Long id) {
        WalkRouteFollowResponse response = walkReportService.getRouteForFollow(id);
        return ResponseEntity.ok(response);
    }

    // 댓글 작성 (root or reply 모두 처리)
    @PostMapping("/reply")
    public void createComment(@AuthenticationPrincipal Member member,
        @RequestBody CommentRequest request) {
        walkReportService.replyCreate(member, request);
    }

    // 리포트별 댓글 조회 (원본 댓글)
    @GetMapping("/reports/{reportId}")
    public List<CommentResponse> getRootComments(@PathVariable Long reportId, Pageable pageable) {
        return walkReportService.getRootComments(reportId, pageable);
    }

    // 특정 댓글의 대댓글 조회
    @GetMapping("/{parentId}/replies")
    public List<ReplyResponse> getReplies(@PathVariable Long parentId) {
        return walkReportService.getReplies(parentId);
    }
}


//    산책 리포트 삭제
//    @DeleteMapping
//    public ResponseEntity<Void> deleteReport(@AuthenticationPrincipal CustomUserDetails user) {
//        return null;
//    }

