package be.stepnote.config;


import static be.stepnote.member.entity.QMember.*;
import static org.assertj.core.api.Assertions.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QuerydslConfig.class)
public class QuerydslConfigTest {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Test
    void querydslFactory_정상생성() {

        long count = queryFactory.selectFrom(member).fetchCount();

        assertThat(count).isEqualTo(0);
    }
}