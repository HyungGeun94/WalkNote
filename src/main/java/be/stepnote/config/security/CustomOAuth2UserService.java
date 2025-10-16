package be.stepnote.config.security;

import be.stepnote.member.entity.Member;
import be.stepnote.member.repository.MemberRepository;
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

            Member member = new Member();
            member.setUsername(username);
            member.setNickname(oAuth2Response.getNickName());
            member.setEmail(oAuth2Response.getEmail());
            member.setProfileImageUrl(oAuth2Response.getProfileImageUrl());
            member.setRole("ROLE_USER");

            memberRepository.save(member);

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
