package be.stepnote.report.comment;


import be.stepnote.global.response.ApiResponse;
import be.stepnote.global.response.SliceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/walk/report/comment")
public class WalkReportCommentController {

    private final WalkReportCommentService walkReportCommentService;


    @PostMapping
    public ApiResponse<?> createReportComment(@RequestBody CommentRequest commentRequest) {

        walkReportCommentService.createComment(commentRequest);


        return ApiResponse.success(null);
    }

    @GetMapping("/rereply/{commentId}")
    public ApiResponse<SliceResponse<CommentResponse>> getReReply(Long commentId,
        @ParameterObject
        @PageableDefault(page = 0, size = 4, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        SliceResponse<CommentResponse> reReply = walkReportCommentService.getReReply(commentId,
            pageable);

        return ApiResponse.success(reReply);
    }

}
