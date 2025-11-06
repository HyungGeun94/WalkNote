//package be.stepnote.follow;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.*;
//
//import be.stepnote.config.security.CustomOAuth2User;
//import be.stepnote.config.security.UserDTO;
//import be.stepnote.member.entity.Member;
//import be.stepnote.member.repository.MemberRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Transactional
//class FollowServiceTest {
//
//
//
//    @Autowired
//    private FollowService followService;
//
//    @Autowired
//    private FollowRepository followRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    private Member me;
//    private Member target;
//
//    @BeforeEach
//    void setUp() {
//
//
//        me = Member.builder()
//            .username("me")
//            .role("ROLE_USER")
//            .build();
//
//        target=Member.builder()
//            .username("target")
//            .role("ROLE_USER")
//            .build();
//
//        memberRepository.save(me);
//        memberRepository.save(target);
//        // Auditing(@CreatedBy) 테스트 위해 SecurityContext 설정
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUsername(me.getUsername());
//        userDTO.setRole(me.getRole());
//
//        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
//
//        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
//
//
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//    }
//
//    @AfterEach
//    void clearContext() {
//        SecurityContextHolder.clearContext();
//    }
//
//
//    @Test
//    @DisplayName(" 팔로우 성공")
//    void follow_success() {
//        // when
//        followService.follow(me, target);
//
//        // then
//        boolean result = followService.isFollowed(me, target);
//        assertThat(result).isTrue();
//        assertThat(followRepository.findAll()).hasSize(1);
//    }
//
//    @Test
//    @DisplayName(" 중복 팔로우 시도 시 예외 발생")
//    void follow_duplicate_fail() {
//        // given
//        followService.follow(me, target);
//
//        // when & then
//        assertThatThrownBy(() -> followService.follow(me, target))
//            .isInstanceOf(IllegalStateException.class)
//            .hasMessage("이미 팔로우 중입니다.");
//    }
//
//    @Test
//    @DisplayName(" 자기 자신 팔로우 시 예외 발생")
//    void follow_self_fail() {
//        assertThatThrownBy(() -> followService.follow(me, me))
//            .isInstanceOf(IllegalArgumentException.class)
//            .hasMessage("자기 자신을 팔로우할 수 없습니다.");
//    }
//
//    @Test
//    @DisplayName(" 언팔로우 성공")
//    void unfollow_success() {
//        // given
//        followService.follow(me, target);
//
//        // when
//        followService.unfollow(target.getId());
//
//        // then
//        boolean result = followService.isFollowed(me, target);
//        assertThat(result).isFalse();
//        assertThat(followRepository.findAll()).isEmpty();
//    }
//}