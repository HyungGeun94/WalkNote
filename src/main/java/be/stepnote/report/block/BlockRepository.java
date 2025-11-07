package be.stepnote.report.block;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlockRepository extends JpaRepository<Block, Long> {


    //  차단 관계 존재 여부 확인 (명시적 조인)
    @Query("""
        select (count(b) > 0)
        from Block b
        join Member blocker on b.blocker.id = blocker.id
        join Member blocked on b.blocked.id = blocked.id
        where blocker.id = :blockerId
          and blocked.id = :blockedId
    """)
    boolean existsByBlockerIdAndBlockedId(
        @Param("blockerId") Long blockerId,
        @Param("blockedId") Long blockedId
    );

    //  차단 관계 삭제
    void deleteByBlockerIdAndBlockedId(Long blockerId, Long blockedId);

    //  내가 차단한 모든 회원 ID 조회 (Feed 조회에서 제외용)
    @Query("""
        select blocked.id
        from Block b
        join Member blocker on b.blocker.id = blocker.id
        join Member blocked on b.blocked.id = blocked.id
        where blocker.id = :blockerId
    """)
    List<Long> findBlockedMemberIdsByBlockerId(@Param("blockerId") Long blockerId);
}
