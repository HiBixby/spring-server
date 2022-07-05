package com.twopow.security.config.oauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.twopow.security.config.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//    private final TokenService tokenService;
//    private final UserRequestMapper userRequestMapper;
//    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
//        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
//        UserDto userDto = userRequestMapper.toDto(oAuth2User);
//
//        log.info("Principal에서 꺼낸 OAuth2User = {}", oAuth2User);
//        // 최초 로그인이라면 회원가입 처리를 한다.
        String targetUrl;
        log.info("토큰 발행 시작");


        ///////////////////////////////////////////////////////////////////
        System.out.println("successfulAuthentication 실행됨: 인증이 완료되었다는 뜻");
        PrincipalDetails principalDetails=(PrincipalDetails) authentication.getPrincipal();

        String jwtToken= JWT.create()
                .withSubject("2-pow Token")
                .withExpiresAt(new Date(System.currentTimeMillis()+(60000) *30)) //30min
                .withClaim("id",principalDetails.getUser().getId())
                .withClaim("username",principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("2powTeam"));
        response.addHeader("Authhorization","Bearer "+jwtToken);



//        Token token = tokenService.generateToken(userDto.getEmail(), "USER");
//        log.info("{}", token);
//        targetUrl = UriComponentsBuilder.fromUriString("/home")
//                .queryParam("token", "token")
//                .build().toUriString();


        //인증 후 oauth2/redirect/+jwtToken 으로 redirect 했을때
        targetUrl=UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect/"+jwtToken)
                .build().toUriString();
//        //인증 후 interview로 redirect했을때
//        targetUrl=UriComponentsBuilder.fromUriString("http://localhost:3000/interview")
//                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}