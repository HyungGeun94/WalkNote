package be.stepnote.report.walk.service;

import be.stepnote.global.response.SliceResponse;
import be.stepnote.member.AuthMemberProvider;
import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import be.stepnote.report.favorite.WalkReportFavorite;
import be.stepnote.report.favorite.WalkReportFavoriteRepository;
import be.stepnote.report.comment.CommentRequest;
import be.stepnote.report.comment.CommentResponse;
import be.stepnote.report.comment.ReplyResponse;
import be.stepnote.report.comment.WalkReportComment;
import be.stepnote.report.comment.WalkReportCommentRepository;
import be.stepnote.report.feed.WalkReportFeedAssembler;
import be.stepnote.report.feed.WalkReportFeedResponse;
import be.stepnote.report.like.WalkReportLikeRepository;
import be.stepnote.report.walk.dto.WalkReportSearchCondition;
import be.stepnote.report.walk.dto.WalkReportSummaryResponse;
import be.stepnote.report.walk.dto.WalkReportUpdateRequest;
import be.stepnote.report.walk.dto.WalkReportUploadRequest;
import be.stepnote.report.walk.dto.WalkReportDetailResponse;
import be.stepnote.report.walk.dto.WalkReportRequest;
import be.stepnote.report.walk.entity.WalkReport;
import be.stepnote.report.walk.repository.WalkReportRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final AuthMemberProvider authMemberProvider;
    private final WalkReportLikeRepository walkReportLikeRepository;


    public Long createReport(WalkReportRequest dto) {

        Member member = authMemberProvider.getCurrentMember();

        WalkReport walkReport = WalkReport.create(
            dto.getDistance(),
            dto.getSteps(),
            dto.getCalorie(),
            dto.getStartTime(),
            dto.getEndTime(),
            dto.getDuration(),
            dto.getTitle(),
            dto.getContent(),
            dto.getStartPoint(),
            dto.getEndPoint(),
            dto.getImages()
        );


        walkReport.createdBy(member);
        walkReportRepository.save(walkReport);

        if (dto.isFavorite()) {
            WalkReportFavorite walkReportFavorite = WalkReportFavorite.create(walkReport);
            walkReportFavoriteRepository.save(walkReportFavorite);
        }

        return walkReport.getId();
    }

    @Transactional(readOnly = true)
    public SliceResponse<WalkReportSummaryResponse> getReports(WalkReportSearchCondition condition) {

//        Pageable pageable,
//        boolean publicVisibility
        Member member = authMemberProvider.getCurrentMember();

        Slice<WalkReport> walkReports = walkReportRepository.findReportSummaries(member,
            condition.toPageable(), condition.isPublicVisibility());

        Integer count = 0;

        if(condition.isPublicVisibility()) {
            List<WalkReport> byCreatedByAndPublicIsTrue = walkReportRepository.findByCreatedByAndIsPublic(
                member, condition.isPublicVisibility());

            count = byCreatedByAndPublicIsTrue.size();
        }
        else{
            List<WalkReport> byCreatedBy = walkReportRepository.findByCreatedBy(member);
            count = byCreatedBy.size();
        }


        Slice<WalkReportSummaryResponse> map = walkReports.map(
            walkReport -> new WalkReportSummaryResponse(walkReport));

        SliceResponse<WalkReportSummaryResponse> walkReportSummaryResponseSliceResponse = SliceResponse.of(
            map, count);

        return walkReportSummaryResponseSliceResponse;
    }

    public SliceResponse<WalkReportSummaryResponse> getMyFavoriteReports(WalkReportSearchCondition condition) {
        Member member = authMemberProvider.getCurrentMember();

        Slice<WalkReport> favorites = walkReportRepository.findMyFavorites(member, condition.toPageable());

        Slice<WalkReportSummaryResponse> map = favorites.map(WalkReportSummaryResponse::new);

        List<WalkReportFavorite> byMember = walkReportFavoriteRepository.findByMember(member);
        int size = byMember.size();


        SliceResponse<WalkReportSummaryResponse> walkReportSliceResponse = SliceResponse.of(map, size);

        return walkReportSliceResponse;
    }


    public WalkReportDetailResponse reportDetail(Long reportId) {

        WalkReport walkReport = walkReportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("리포트를 찾을 수 없습니다."));

        Member member = authMemberProvider.getCurrentMember();


        Boolean isLike = walkReportLikeRepository.existsByWalkReportAndMember(walkReport,member).orElse(false);
        Boolean isFavorite = walkReportFavoriteRepository.existsByWalkReportAndMember(walkReport, member)
            .orElse(false);

        WalkReportDetailResponse walkReportDetailResponse = new WalkReportDetailResponse(
            walkReport,isLike,isFavorite);


        return walkReportDetailResponse;

    }

    public WalkReportDetailResponse getReportForEdit(Long reportId) {
        WalkReport report = walkReportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("리포트를 찾을 수 없습니다."));

        Member member = authMemberProvider.getCurrentMember();

        if (!report.getCreatedBy().getUsername().equals(member.getUsername())) {
            throw new AccessDeniedException("본인 게시글만 수정할 수 있습니다.");
        }

        return new WalkReportDetailResponse(report);
    }

    public List<WalkReportFeedResponse> getFeed(Pageable pageable, String username) {
        Member me = memberRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        List<WalkReport> reports = walkReportRepository.findAll(pageable).getContent();

        return feedAssembler.assemble(reports, me);
    }


    public void updateReport(Long reportId, WalkReportUpdateRequest request) {
        WalkReport report = walkReportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("리포트를 찾을 수 없습니다."));

        Member member = authMemberProvider.getCurrentMember();
        if (!report.getCreatedBy().getUsername().equals(member.getUsername())) {
            throw new AccessDeniedException("본인 게시글만 수정할 수 있습니다.");
        }

        report.update(request.getTitle(), request.getContent(), request.getImageUrls());

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

    public SliceResponse<CommentResponse> getRootComments(Long reportId, Pageable pageable) {
        WalkReport report = walkReportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("리포트 없음"));

        Slice<WalkReportComment> slice = commentRepository
            .findByWalkReportAndParentIsNull(report, pageable);

        Map<Long, Long> longLongMap = commentRepository.countCommentsByReportIds(List.of(reportId));

        int count = longLongMap.getOrDefault(reportId,0L).intValue();

        Slice<CommentResponse> map = slice.map(s -> CommentResponse.from(s,commentRepository.countByParent(s)));

        SliceResponse<CommentResponse> commentResponseSliceResponse = SliceResponse.of(map, count);

        return commentResponseSliceResponse;
    }

    public List<ReplyResponse> getReplies(Long parentId) {
        WalkReportComment parent = commentRepository.findById(parentId)
            .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));

        return commentRepository.findByParentOrderByCreatedAtAsc(parent)
            .stream()
            .map(ReplyResponse::from)
            .toList();
    }


    public void deleteReport(Long reportId) {

        Member member = authMemberProvider.getCurrentMember();

        WalkReport report = walkReportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("리포트를 찾을 수 없습니다."));

        if (!report.getCreatedBy().getUsername().equals(member.getUsername())) {
            throw new AccessDeniedException("본인 게시글만 삭제할 수 있습니다.");
        }

        report.changeActive();




    }

    public boolean authCeheck(Long reportId) {

        Member member = authMemberProvider.getCurrentMember();

        Optional<WalkReport> byId = walkReportRepository.findById(reportId);

        WalkReport walkReport = byId.orElseThrow();


        return walkReport.getCreatedBy().getUsername().equals(member.getUsername());



    }

    public void uploadRort(Long reportId, WalkReportUploadRequest request) {


        WalkReport report = walkReportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("리포트를 찾을 수 없습니다."));

        Member member = authMemberProvider.getCurrentMember();
        if (!report.getCreatedBy().getUsername().equals(member.getUsername())) {
            throw new AccessDeniedException("본인 게시글만 수정할 수 있습니다.");
        }

        report.upload(request.getTitle(), request.getContent(), request.getImageUrls(),
            request.isPublicStatus(), request.isStaticHideStatus());






    }
}
