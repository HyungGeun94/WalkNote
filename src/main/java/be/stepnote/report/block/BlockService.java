package be.stepnote.report.block;

import be.stepnote.follow.MemberSimpleResponse;
import be.stepnote.member.AuthMemberProvider;
import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BlockService {

    private final BlockRepository blockRepository;
    private final MemberRepository memberRepository;
    private final AuthMemberProvider authMemberProvider;

    //  차단 등록
    public void blockMember(Long blockedId) {
        Member blocker = authMemberProvider.getCurrentMember();
        Member blocked = memberRepository.findById(blockedId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (blockRepository.existsByBlockerIdAndBlockedId(blocker.getId(), blockedId)) {
            throw new IllegalStateException("이미 차단한 회원입니다.");
        }

        Block block = Block.create(blocker, blocked);
        blockRepository.save(block);
    }

    // 차단 해제
    public void unblockMember(Long blockedId) {
        Member blocker = authMemberProvider.getCurrentMember();

        if (!blockRepository.existsByBlockerIdAndBlockedId(blocker.getId(), blockedId)) {
            throw new IllegalStateException("차단되지 않은 회원입니다.");
        }

        blockRepository.deleteByBlockerIdAndBlockedId(blocker.getId(), blockedId);
    }

    @Transactional(readOnly = true)
    public List<MemberSimpleResponse> getBlockedMembers() {
        Member me = authMemberProvider.getCurrentMember();
        List<Member> blockedMembers = memberRepository.findBlockedMembers(me.getId());

        return blockedMembers.stream()
            .map(MemberSimpleResponse::from)
            .toList();
    }
}
