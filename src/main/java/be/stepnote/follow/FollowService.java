package be.stepnote.follow;


import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    /**
     * 팔로우 하기
     */
    public void follow(Member follower, Member following) {
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
    public void unfollow(Member follower, Member following) {
        if (!isFollowed(follower, following)) {
            throw new IllegalStateException("팔로우 중이 아닙니다.");
        }

        followRepository.deleteByFollowerAndFollowing(follower, following);
    }

    /**
     * 팔로우 여부 확인
     */
    public boolean isFollowed(Member follower, Member following) {
        return followRepository.existsByFollowerAndFollowing(follower, following);
    }

    public List<MemberSimpleResponse> getFollowers(String username) {
        Member me = memberRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        List<Long> followerIds = followRepository.findFollowerIdsByFollowingId(me.getId());
        List<Member> followers = memberRepository.findAllById(followerIds);

        return
            followers.stream()
                .map(MemberSimpleResponse::from)
                .toList();
    }

    public List<MemberSimpleResponse> getFollowings(String username) {
        Member me = memberRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(me.getId());
        List<Member> followings = memberRepository.findAllById(followingIds);

        return
            followings.stream()
                .map(MemberSimpleResponse::from)
                .toList();
    }
}
