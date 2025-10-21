package be.stepnote.config.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JWTUtilTest {

    private JWTUtil jwtUtil;

    @BeforeEach
    void setUp() {
        // secret이 환경변수에 안 들어가 있어도 직접 주입 가능
        jwtUtil = new JWTUtil("test-secret-key-for-jwt12314123312");
    }

    @Test
    void 토큰_생성_후_파싱_성공() {
        // given
        String token = jwtUtil.createJwt("testUser", "ROLE_USER", 1000L * 60);

        // when
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        Boolean expired = jwtUtil.isExpired(token);

        // then
        assertThat(username).isEqualTo("testUser");
        assertThat(role).isEqualTo("ROLE_USER");
        assertThat(expired).isFalse();
    }

    @Test
    void 만료된_토큰은_true_반환() throws InterruptedException {
        // given
        String token = jwtUtil.createJwt("testUser", "ROLE_USER", 1L); // 1ms 만료

        Thread.sleep(5);
        Boolean expired= false;
        // when
        try {
             expired= jwtUtil.isExpired(token);
        }catch (ExpiredJwtException e) {

            expired= true;
        }

        // then
        assertThat(expired).isTrue();
    }
}