package com.twopow.security.config.oauth;

import com.twopow.security.config.auth.PrincipalDetails;
import com.twopow.security.model.User;
import com.twopow.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    //구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
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
        String provider = userRequest.getClientRegistration().getClientId();//google
        String providerId=oauth2User.getAttribute("sub");
        String username=provider+"_"+providerId;
        String password= new BCryptPasswordEncoder().encode("이에이승팀테스트용비밀키");
        String role="ROLE_USER";
        String email=oauth2User.getAttribute("email");

        User userEntity = userRepository.findByUsername(username);
        if(userEntity==null){ //가입한적 없는경우
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

        //return super.loadUser(userRequest);
        return new PrincipalDetails(userEntity,oauth2User.getAttributes());
    }
}
