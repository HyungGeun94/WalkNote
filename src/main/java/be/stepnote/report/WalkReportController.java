package be.stepnote.report;

import be.stepnote.config.security.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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

//    산책 리포트 업데이트
//    @PatchMapping()
//    public void updateReport(){
//    }

//    산책 리포트 삭제
//    @DeleteMapping
//    public ResponseEntity<Void> deleteReport(@AuthenticationPrincipal CustomUserDetails user) {
//        return null;
//    }

    // “따라가기용” 경로 조회 API
//    @GetMapping("/{id}/path")
//    public ResponseEntity<List<WalkCoordinate>> getPath(@PathVariable Long id) {
//        List<WalkCoordinate> coordinates = walkReportService.getCoordinates(id);
//        return ResponseEntity.ok(coordinates);
//    }
}
