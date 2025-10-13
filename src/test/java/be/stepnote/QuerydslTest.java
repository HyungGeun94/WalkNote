package be.stepnote;


import static be.stepnote.member.QUserEntity.*;
import static org.assertj.core.api.Assertions.*;

import be.stepnote.member.QUserEntity;
import be.stepnote.member.UserEntity;
import be.stepnote.member.UserRepository;
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
    UserRepository userRepository;


    @Test
    public void test() {

        List<UserEntity> list = queryFactory.select(userEntity)
            .from(userEntity)
            .fetch();

        assertThat(list.size()).isEqualTo(1);

        assertThat(list.get(0).getUsername()).isEqualTo("test");


    }

    @Test
    public void test2() {

        List<UserEntity> allUsers = userRepository.findAllUsers();

        assertThat(allUsers.size()).isEqualTo(1);
        assertThat(allUsers.get(0).getUsername()).isEqualTo("test");

    }
}
