package be.stepnote.follow;

import be.stepnote.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 이미 팔로우 중인지 확인
    boolean existsByFollowerAndFollowing(Member follower, Member following);

    // 내가 팔로우한 사람들 ID만 추출
    @Query("select f.following.id from Follow f where f.follower.id = :followerId")
    List<Long> findFollowingIdsByFollowerId(@Param("followerId") Long followerId);

    // 나를 팔로우한 사람들 ID만 추출
    @Query("select f.follower.id from Follow f where f.following.id = :followingId")
    List<Long> findFollowerIdsByFollowingId(@Param("followingId") Long followingId);

    // 언팔로우 (삭제)
    void deleteByFollowerAndFollowing(Member follower, Member following);


    Long countByFollowing(Member following);

    Long countByFollower(Member follower);



}
