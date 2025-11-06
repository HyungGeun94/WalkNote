package be.stepnote.follow;


import be.stepnote.global.response.SliceResponse;
import be.stepnote.member.AuthMemberProvider;
import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import be.stepnote.report.walk.dto.WalkReportSearchCondition;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final AuthMemberProvider authMemberProvider;

    /**
     * 팔로우 하기
     */
    public void follow(Long userId) {

        Member follower = authMemberProvider.getCurrentMember();

        Member following = memberRepository.findById(userId).orElseThrow();

        // 자기 자신은 팔로우 불가
        if (follower.getId().equals(following.getId())) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        // 이미 팔로우 중이면 예외
        if (isFollowed(follower, following)) {
            throw new IllegalStateException("이미 팔로우 중입니다.");
        }

        Follow follow = Follow.create(follower, following);
        followRepository.save(follow);
    }

    /**
     * 언팔로우 하기
     */
    public void unfollow(Long userId)  {

        Member follower = authMemberProvider.getCurrentMember();

        Member following = memberRepository.findById(userId).orElseThrow();

        if (!isFollowed(follower, following)) {
            throw new RuntimeException("팔로우 중이 아닙니다.");
        }

        followRepository.deleteByFollowerAndFollowing(follower, following);
    }

    /**
     * 팔로우 여부 확인
     */
    public boolean isFollowed(Member follower, Member following) {
        return followRepository.existsByFollowerAndFollowing(follower, following);
    }

    public SliceResponse<MemberSimpleResponse> getFollowers(WalkReportSearchCondition condition) {

        Member me = authMemberProvider.getCurrentMember();

        Slice<Long> followerIds = followRepository.findFollowerIdsByFollowingId(me.getId(),condition.toPageable());
        List<Member> followers = memberRepository.findAllById(followerIds.getContent());

        Long count = followRepository.countByFollowing(me);

        List<MemberSimpleResponse> list = followers.stream().map(MemberSimpleResponse::from)
            .toList();

        SliceResponse<MemberSimpleResponse> sliceResponse = new SliceResponse(list, followerIds.hasNext(),
            followerIds.getNumber(), followerIds.getSize(), count.intValue());

        return sliceResponse;
    }

    public SliceResponse<MemberSimpleResponse> getFollowings(WalkReportSearchCondition condition) {

        Member me = authMemberProvider.getCurrentMember();

        Slice<Long> followingIds = followRepository.findFollowingIdsByFollowerId(me.getId(),condition.toPageable());
        List<Member> followings = memberRepository.findAllById(followingIds.getContent());

        Long count = followRepository.countByFollower(me);

        List<MemberSimpleResponse> list = followings.stream().map(MemberSimpleResponse::from)
            .toList();

        SliceResponse<MemberSimpleResponse> sliceResponse = new SliceResponse<>(
            list, followingIds.hasNext(), followingIds.getNumber(), followingIds.getSize(),
            count.intValue());

        return
            sliceResponse;
    }
}
