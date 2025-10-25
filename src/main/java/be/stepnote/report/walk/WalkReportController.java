package be.stepnote.report.walk;

import be.stepnote.config.security.CustomOAuth2User;
import be.stepnote.global.response.ApiResponse;
import be.stepnote.global.response.SliceResponse;
import be.stepnote.member.entity.Member;
import be.stepnote.report.comment.CommentRequest;
import be.stepnote.report.comment.CommentResponse;
import be.stepnote.report.comment.ReplyResponse;
import be.stepnote.report.feed.WalkReportFeedResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    private final int DEFAULT_PAGE_SIZE = 4;

    private final WalkReportService walkReportService;


    /**
     * 산책 리포트 생성
     */
    @PostMapping
    public ApiResponse<Long> createReport(
        @RequestBody WalkReportRequest request,
        @AuthenticationPrincipal CustomOAuth2User user
    ) {

        Long reportId = walkReportService.createReport(request,user.getUsername());

        return ApiResponse.success(reportId);
    }


    /**
     * 산책 리포트 조회
     * publicVisibility = true면 산책 리포트 공개 피드로 한것만 조회
     *
     * publicVisibility = false면 산책 리포트 전체 조회
     */
    @GetMapping("/list")
    public ApiResponse<SliceResponse<WalkReportSummaryResponse>> getReports(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "LATEST") String sort,
        @AuthenticationPrincipal CustomOAuth2User user,
        @RequestParam(defaultValue = "false") boolean publicVisibility
    ) {

        Sort sortOption = sort.equalsIgnoreCase("OLDEST")
            ? Sort.by(Sort.Direction.ASC, "createdAt")
            : Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE, sortOption);

        SliceResponse<WalkReportSummaryResponse> reports = walkReportService.getReports(pageable,
            user.getUsername(), publicVisibility);

        return ApiResponse.success(reports);
    }

    /**
     * 저장한 게시물( 즐겨찾기 )
     */

    @GetMapping("/list/favorites")
    public ApiResponse<SliceResponse<WalkReportSummaryResponse>> getMyFavorites(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "LATEST") String sort,
        @AuthenticationPrincipal CustomOAuth2User user
        ) {
        Sort sortOption = sort.equalsIgnoreCase("OLDEST")
            ? Sort.by(Sort.Direction.ASC, "createdAt")
            : Sort.by(Sort.Direction.DESC, "createdAt");

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE, sortOption);

        SliceResponse<WalkReportSummaryResponse> myFavoriteReports = walkReportService.getMyFavoriteReports(
            pageable, user.getUsername());

        return ApiResponse.success(myFavoriteReports);
    }

    /**
     * 산책 리포트 상세
     */
    @GetMapping("/detail/{reportId}")
    public ApiResponse<WalkReportDetailResponse> getReportDetail(

        @PathVariable Long reportId

    ) {

        WalkReportDetailResponse walkReportDetailResponse = walkReportService.reportDetail(
            reportId);

        return ApiResponse.success(walkReportDetailResponse);
    }

    @GetMapping("/authCheck/{reportId}")
    public ApiResponse<Boolean> authCheck(@PathVariable Long reportId) {

        return ApiResponse.success(walkReportService.authCeheck(reportId));
    }

    @GetMapping("/feed")
    public List<WalkReportFeedResponse> getFeed(
        @AuthenticationPrincipal CustomOAuth2User user,
        Pageable pageable
    ) {
        return walkReportService.getFeed(pageable, user.getUsername());
    }


    /**
     * 산책 리포트 수정
     */

    @PatchMapping("/edit/{id}")
    public ApiResponse<Void> updateReport(
        @PathVariable Long id,
        @RequestBody WalkReportUpdateRequest request
    ) {
        walkReportService.updateReport(id, request);
        return ApiResponse.success(null);
    }

    /**
     * 리포트 수정 전 권한요청 및 정보 주기
     */
    @GetMapping("/edit/{id}")
    public ApiResponse<WalkReportDetailResponse> getReportForEdit(
        @AuthenticationPrincipal CustomOAuth2User me,
        @PathVariable Long id
    ) {
        WalkReportDetailResponse response = walkReportService.getReportForEdit(me.getUsername(), id);
        return ApiResponse.success(response);
    }

    @PatchMapping("/upload/{reportId}")
    public ApiResponse<Void> uploadReport(
        @PathVariable Long reportId,
        @RequestBody WalkReportUploadRequest walkReportUploadRequest
    ){
        walkReportService.uploadRort(reportId, walkReportUploadRequest);

        return ApiResponse.success(null);
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

    //    산책 리포트 삭제
    @DeleteMapping("/{reportId}")
    public ApiResponse<?> deleteReport(@PathVariable Long reportId) {

        walkReportService.deleteReport(reportId);
        return ApiResponse.success(null);
    }
}




