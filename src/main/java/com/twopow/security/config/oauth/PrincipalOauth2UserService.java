package com.twopow.security.config.oauth;

import com.twopow.security.config.auth.PrincipalDetails;
import com.twopow.security.config.oauth.provider.FacebookUserInfo;
import com.twopow.security.config.oauth.provider.GoogleUserInfo;
import com.twopow.security.config.oauth.provider.NaverUserInfo;
import com.twopow.security.config.oauth.provider.OAuth2UserInfo;
import com.twopow.security.model.User;
import com.twopow.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    //구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //구글 로그인 버튼 클릭->구글 로그인창->로그인을 완료->code를 리턴(Oauth-client 라이브러리)->AccessToken 요청
        //userRequest정보->loadUser함수 호출->구글로부터 회원 프로필 받아준다.
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttributes:" + oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            log.trace("구글 로그인 요청이 들어왔습니다.");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            log.trace("네이버 로그인 요청이 들어왔습니다.");
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else {
            log.error("지원하지 않는 플랫폼 입니다.");
        }


        Optional<User> userOptional = userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            // user가 존재하면 update 해주기
            user.setPicture(oAuth2UserInfo.getPicture());
            user.setEmail(oAuth2UserInfo.getEmail());
            userRepository.save(user);
        } else {
            // user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음.
            user = User.builder()
                    .name(oAuth2UserInfo.getName())
                    .username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                    .email(oAuth2UserInfo.getEmail())
                    .role("ROLE_USER").provider(oAuth2UserInfo.getProvider())
                    .providerId(oAuth2UserInfo.getProviderId())
                    .picture(oAuth2UserInfo.getPicture())
                    .build();
            userRepository.save(user);
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
