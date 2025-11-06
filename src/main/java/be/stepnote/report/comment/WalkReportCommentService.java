package be.stepnote.report.comment;


import be.stepnote.alarm.NotificationService;
import be.stepnote.global.response.SliceResponse;
import be.stepnote.member.AuthMemberProvider;
import be.stepnote.member.entity.Member;
import be.stepnote.report.walk.entity.WalkReport;
import be.stepnote.report.walk.repository.WalkReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WalkReportCommentService {

    private final WalkReportCommentRepository walkReportCommentRepository;

    private final AuthMemberProvider authMemberProvider;

    private final WalkReportRepository walkReportRepository;

    private final NotificationService notificationService;



    public void createComment(CommentRequest commentRequest) {

        Long reportId = commentRequest.getReportId();
        Long parentId = commentRequest.getParentId();
        String content = commentRequest.getContent();

        WalkReport walkReport = walkReportRepository.findById(reportId)
            .orElseThrow();


        Member member = authMemberProvider.getCurrentMember();


        WalkReportComment rootComment;

        if(parentId == null){

             rootComment = WalkReportComment.createRoot(walkReport, member, content);

        }else{
            WalkReportComment walkReportComment = walkReportCommentRepository.findById(parentId)
                .orElseThrow();


            rootComment = WalkReportComment.createReply(walkReport, member, content,
                walkReportComment);

        }

        walkReportCommentRepository.save(rootComment);

        notificationService.handleCommentNotification(walkReport, member, commentRequest.getContent());

    }


    public SliceResponse<CommentResponse> getReReply(Long commentId, Pageable pageable) {

        WalkReportComment parentComment = walkReportCommentRepository.findById(commentId)
            .orElseThrow();

        Slice<WalkReportComment> byParent = walkReportCommentRepository.findByParent(parentComment,
            pageable);

        Slice<CommentResponse> map = byParent.map(s -> CommentResponse.from(s, 0L));

        SliceResponse<CommentResponse> walkReportCommentSliceResponse = SliceResponse.of(map,
            0);

        return walkReportCommentSliceResponse;


    }
}
