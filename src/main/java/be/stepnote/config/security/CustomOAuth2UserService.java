package be.stepnote.config.security;

import be.stepnote.member.entity.Member;
import be.stepnote.member.entity.NotificationSetting;
import be.stepnote.member.repository.MemberRepository;
import be.stepnote.member.repository.NotificationSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    private final NotificationSettingRepository notificationSettingRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("kakao")) {

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());

        } else {

            return null;

        }

        String username = oAuth2Response.getProvider() + "" + oAuth2Response.getProviderId();
        Member existData = memberRepository.findByUsername(username).orElse(null);

        if (existData == null) {

            Member member = Member.builder()
                .username(username)
                .nickname(oAuth2Response.getNickName())
                .email(oAuth2Response.getEmail())
                .profileImageUrl(oAuth2Response.getProfileImageUrl())
                .role("ROLE_USER")
                .build();

            memberRepository.save(member);

            notificationSettingRepository.save(NotificationSetting.createDefault(member));

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setNickname(oAuth2Response.getNickName());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        } else {

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setNickname(existData.getNickname());
            userDTO.setRole(existData.getRole());

            return new CustomOAuth2User(userDTO);
        }
    }
}
