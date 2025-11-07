package be.stepnote.member.repository;


import static be.stepnote.member.entity.QMember.member;

import be.stepnote.member.entity.Member;
import be.stepnote.member.entity.QMember;
import be.stepnote.report.block.QBlock;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;



    @Override
    public List<Member> findAllUsers() {
        return queryFactory.select(member)
            .from(member)
            .fetch();
    }

    @Override
    public List<Member> findBlockedMembers(Long blockerId) {
        QMember member = QMember.member;
        QBlock block = QBlock.block;

        return queryFactory
            .selectFrom(member)
            .join(block).on(member.id.eq(block.blocked.id))
            .where(block.blocker.id.eq(blockerId))
            .orderBy(member.nickname.asc())
            .fetch();
    }
}
