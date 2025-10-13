package be.stepnote.member.repository;


import static be.stepnote.member.entity.QMember.member;

import be.stepnote.member.entity.Member;
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
}
