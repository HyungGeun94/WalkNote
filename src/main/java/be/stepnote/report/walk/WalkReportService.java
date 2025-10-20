package be.stepnote.report.walk;

import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import be.stepnote.report.favorite.WalkReportFavorite;
import be.stepnote.report.favorite.WalkReportFavoriteRepository;
import be.stepnote.report.image.WalkReportImage;
import be.stepnote.report.WalkRouteFollowResponse;
import be.stepnote.report.comment.CommentRequest;
import be.stepnote.report.comment.CommentResponse;
import be.stepnote.report.comment.ReplyResponse;
import be.stepnote.report.comment.WalkReportComment;
import be.stepnote.report.comment.WalkReportCommentRepository;
import be.stepnote.report.feed.WalkReportFeedAssembler;
import be.stepnote.report.feed.WalkReportFeedResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WalkReportService {

    private final WalkReportRepository walkReportRepository;
    private final MemberRepository memberRepository;
    private final WalkReportFavoriteRepository walkReportFavoriteRepository;
    private final WalkReportFeedAssembler feedAssembler;
    private final WalkReportCommentRepository commentRepository;

    public Long createReport(WalkReportRequest dto) {

        WalkReport walkReport = WalkReport.create(dto);

        WalkReport saved = walkReportRepository.save(walkReport);

        if (dto.isFavorite()) {
            WalkReportFavorite walkReportFavorite = WalkReportFavorite.create(walkReport);
            walkReportFavoriteRepository.save(walkReportFavorite);
        }

        return saved.getId();
    }

    @Transactional(readOnly = true)
    public Slice<WalkReportSummaryResponse> getReports(Pageable pageable, String username,
        boolean publicVisibility) {
        Member member = memberRepository.findByUsername(username).orElseThrow();

        Slice<WalkReport> walkReports = walkReportRepository.findReportSummaries(member,
            pageable, publicVisibility);

        return walkReports.map(walkReport -> new WalkReportSummaryResponse(walkReport));
    }

    public Slice<WalkReportSummaryResponse> getMyFavoriteReports(Pageable pageable,
        String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow();

        Slice<WalkReport> favorites = walkReportRepository.findMyFavorites(member, pageable);

        return favorites.map(WalkReportSummaryResponse::new);
    }

    public List<WalkReportFeedResponse> getFeed(Pageable pageable, String username) {
        Member me = memberRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        List<WalkReport> reports = walkReportRepository.findAll(pageable).getContent();

        return feedAssembler.assemble(reports, me);
    }

    public void toggleVisibility(String userName, Long reportId) {
        WalkReport report = walkReportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("리포트를 찾을 수 없습니다."));

        if (!report.getCreatedBy().getUsername().equals(userName)) {
            throw new AccessDeniedException("본인 리포트만 공개 상태를 변경할 수 있습니다.");
        }

        report.toggleVisibility();
    }

    public void updateReport(String username, Long reportId, WalkReportUpdateRequest request) {
        WalkReport report = walkReportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("리포트를 찾을 수 없습니다."));

        if (!report.getCreatedBy().getUsername().equals(username)) {
            throw new AccessDeniedException("본인 게시글만 수정할 수 있습니다.");
        }

        report.update(request.getTitle(), request.getContent(), request.getImageUrls());

    }

    public WalkReportEditResponse getReportForEdit(String username, Long reportId) {
        WalkReport report = walkReportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("리포트를 찾을 수 없습니다."));

        if (!report.getCreatedBy().getUsername().equals(username)) {
            throw new AccessDeniedException("본인 게시글만 수정할 수 있습니다.");
        }

        return WalkReportEditResponse.builder()
            .title(report.getTitle())
            .content(report.getContent())
            .imageUrls(report.getImages().stream()
                .map(WalkReportImage::getUrl)
                .toList())
            .build();
    }

    public WalkRouteFollowResponse getRouteForFollow(Long reportId) {
        WalkReport report = walkReportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("리포트를 찾을 수 없습니다."));

        return WalkRouteFollowResponse.builder()
            .reportId(report.getId())
            .title(report.getTitle())
            .content(report.getContent())
            .authorNickname(report.getCreatedBy().getNickname())
            .build();
    }


    public void replyCreate(Member author, CommentRequest request) {
        WalkReport report = walkReportRepository.findById(request.getReportId())
            .orElseThrow(() -> new IllegalArgumentException("리포트 없음"));

        if (request.getParentId() == null) {
            WalkReportComment root = WalkReportComment.createRoot(report, author , request.getContent());
            commentRepository.save(root);
        } else {
            WalkReportComment parent = commentRepository.findById(request.getParentId())
                .orElseThrow(() -> new IllegalArgumentException("부모 댓글 없음"));
            WalkReportComment reply = WalkReportComment.createReply(author, report, parent, request.getContent());
            commentRepository.save(reply);
        }
    }

    public List<CommentResponse> getRootComments(Long reportId, Pageable pageable) {
        WalkReport report = walkReportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("리포트 없음"));

        List<WalkReportComment> roots = commentRepository
            .findByWalkReportAndParentIsNullOrderByCreatedAtDesc(report, pageable);

        return roots.stream()
            .map(c -> CommentResponse.from(c, commentRepository.countByParent(c)))
            .toList();
    }

    public List<ReplyResponse> getReplies(Long parentId) {
        WalkReportComment parent = commentRepository.findById(parentId)
            .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));

        return commentRepository.findByParentOrderByCreatedAtAsc(parent)
            .stream()
            .map(ReplyResponse::from)
            .toList();
    }


}
