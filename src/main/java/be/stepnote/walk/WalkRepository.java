package be.stepnote.walk;

import be.stepnote.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkRepository extends JpaRepository<Walk, Long> {

    // 유저 기준으로 최근 산책 1개 (가장 최신순)
    Optional<Walk> findFirstByMemberOrderByStartedAtDesc(Member user);

    // 아직 종료되지 않은 진행 중 산책
    Optional<Walk> findFirstByMemberAndEndedAtIsNullOrderByStartedAtDesc(Member member);

}
