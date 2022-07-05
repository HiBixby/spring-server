package com.twopow.security.config.oauth;

import com.twopow.security.config.auth.PrincipalDetails;
import com.twopow.security.config.oauth.provider.FacebookUserInfo;
import com.twopow.security.config.oauth.provider.GoogleUserInfo;
import com.twopow.security.config.oauth.provider.NaverUserInfo;
import com.twopow.security.config.oauth.provider.OAuth2UserInfo;
import com.twopow.security.model.User;
import com.twopow.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    //구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //System.out.println("userRequest:"+userRequest.getClientRegistration());//registrationId로 어떤 OAuth로 로그인 했는지 확인 가능.
        //System.out.println("userRequest:"+userRequest.getClientRegistration().getRegistrationId());
        //System.out.println("userRequest:"+userRequest.getAccessToken().getTokenValue());//토큰은 별로 안중요
        //구글 로그인 버튼 클릭->구글 로그인창->로그인을 완료->code를 리턴(Oauth-client 라이브러리)->AccessToken 요청
        //userRequest정보->loadUser함수 호출->구글로부터 회원 프로필 받아준다.
        //System.out.println("userRequest:"+super.loadUser(userRequest).getAttributes());
        OAuth2User oauth2User=super.loadUser(userRequest);
        System.out.println("getAttributes:"+oauth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo=null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo=new GoogleUserInfo(oauth2User.getAttributes());
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo=new FacebookUserInfo(oauth2User.getAttributes());
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo=new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
        }
        else{
            System.out.println("지원하지 않는 플랫폼 입니다.");
        }


        String provider = oAuth2UserInfo.getProvider();
        System.out.println(provider);
        String providerId=oAuth2UserInfo.getProviderId();
        System.out.println(providerId);
        String username=provider+"_"+providerId;
        System.out.println(username);
        String password= new BCryptPasswordEncoder().encode("이에이승팀테스트용비밀키");
        System.out.println(password);
        String role="ROLE_USER";
        String email=oAuth2UserInfo.getEmail();
        System.out.println(email);

        User userEntity = userRepository.findByUsername(username);
        if(userEntity==null){ //가입한적 없는경우
            System.out.println("첫번째 로그인");
            userEntity=User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }
        else{
            System.out.println("로그인 이미 한적이 있음");
        }

        //return super.loadUser(userRequest);
        return new PrincipalDetails(userEntity,oauth2User.getAttributes());
    }
}
