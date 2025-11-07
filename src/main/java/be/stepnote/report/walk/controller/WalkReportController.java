package be.stepnote.report.walk.controller;

import be.stepnote.config.security.CustomOAuth2User;
import be.stepnote.global.response.ApiResponse;
import be.stepnote.global.response.SliceResponse;
import be.stepnote.member.entity.Member;
import be.stepnote.report.comment.CommentRequest;
import be.stepnote.report.comment.CommentResponse;
import be.stepnote.report.comment.ReplyResponse;
import be.stepnote.report.feed.WalkReportFeedResponse;
import be.stepnote.report.walk.dto.WalkReportDetailResponse;
import be.stepnote.report.walk.dto.WalkReportRequest;
import be.stepnote.report.walk.dto.WalkReportSearchCondition;
import be.stepnote.report.walk.service.WalkReportService;
import be.stepnote.report.walk.dto.WalkReportSummaryResponse;
import be.stepnote.report.walk.dto.WalkReportUpdateRequest;
import be.stepnote.report.walk.dto.WalkReportUploadRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    private static final int DEFAULT_PAGE_SIZE = 4;

    private final WalkReportService walkReportService;


    /**
     * 산책 리포트 생성
     */
    @PostMapping
    public ApiResponse<Long> createReport(
        @RequestBody WalkReportRequest request) {

        Long reportId = walkReportService.createReport(request);

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
        @ModelAttribute WalkReportSearchCondition condition
    ) {

        SliceResponse<WalkReportSummaryResponse> reports = walkReportService.getReports(condition);

        return ApiResponse.success(reports);
    }

    /**
     * 저장한 게시물( 즐겨찾기 )
     */

    @GetMapping("/list/favorites")
    public ApiResponse<SliceResponse<WalkReportSummaryResponse>> getMyFavorites(
        @ModelAttribute WalkReportSearchCondition condition
        ) {

        SliceResponse<WalkReportSummaryResponse> myFavoriteReports = walkReportService.getMyFavoriteReports(
            condition);

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

        boolean bool = walkReportService.authCeheck(reportId);

        return ApiResponse.success(bool);
    }

    /**
     * 산책 리포트 수정
     */

    @PatchMapping("/edit/{id}")
    public ApiResponse<Boolean> updateReport(
        @PathVariable Long id,
        @RequestBody WalkReportUpdateRequest request
    ) {
        walkReportService.updateReport(id, request);
        return ApiResponse.success(true);
    }

    /**
     * 리포트 수정 전 권한요청 및 정보 주기
     */
    @GetMapping("/edit/{id}")
    public ApiResponse<WalkReportDetailResponse> getReportForEdit(
        @PathVariable Long id
    ) {
        WalkReportDetailResponse response = walkReportService.getReportForEdit(id);
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



    //    산책 리포트 삭제
    @DeleteMapping("/{reportId}")
    public ApiResponse<?> deleteReport(@PathVariable Long reportId) {

        walkReportService.deleteReport(reportId);
        return ApiResponse.success(null);
    }


    // 리포트별 댓글 조회 (원본 댓글)
    @GetMapping("/reply/{reportId}")
    public ApiResponse<SliceResponse<CommentResponse>> getRootComments(
        @PathVariable Long reportId,
        @ParameterObject
        @PageableDefault(page = 0, size = 4, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        SliceResponse<CommentResponse> rootComments = walkReportService.getRootComments(reportId,
            pageable);
        return ApiResponse.success(rootComments);
    }





    // TODO 아래부분 API 확정 및 테스트 코드 작성


    // 댓글 작성 (root or reply 모두 처리)
//    @PostMapping("/reply")
//    public void createComment(@AuthenticationPrincipal Member member,
//        @RequestBody CommentRequest request) {
//        walkReportService.replyCreate(member, request);
//    }

    // 특정 댓글의 대댓글 조회
    @GetMapping("/{parentId}/replies")
    public List<ReplyResponse> getReplies(@PathVariable Long parentId) {
        return walkReportService.getReplies(parentId);
    }

    @GetMapping("/feed")
    public List<WalkReportFeedResponse> getFeed(
        Pageable pageable
    ) {
        return walkReportService.getFeed(pageable);
    }

    @DeleteMapping("/favorites/del")
    public ApiResponse<Void> deleteFavorites(@RequestBody List<Long> ids){

        walkReportService.deleteFavorites(ids);


        return ApiResponse.success(null);
    }

}




