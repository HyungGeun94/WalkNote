package be.stepnote;


import static be.stepnote.member.entity.QMember.*;
import static org.assertj.core.api.Assertions.*;

import be.stepnote.member.entity.Member;
import be.stepnote.member.entity.QMember;
import be.stepnote.member.repository.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class QuerydslTest {

    @Autowired
    EntityManager em;

    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    MemberRepository memberRepository;


    @Test
    public void test() {

        List<Member> list = queryFactory.select(member)
            .from(member)
            .fetch();

        assertThat(list.size()).isEqualTo(1);

        assertThat(list.get(0).getUsername()).isEqualTo("test");


    }

    @Test
    public void test2() {

        List<Member> allUsers = memberRepository.findAllUsers();

        assertThat(allUsers.size()).isEqualTo(1);
        assertThat(allUsers.get(0).getUsername()).isEqualTo("test");

    }
}
