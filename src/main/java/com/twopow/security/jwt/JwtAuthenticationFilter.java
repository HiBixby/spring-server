package com.twopow.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twopow.security.config.auth.PrincipalDetails;
import com.twopow.security.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

// /login요청해서 username,password 전송하면(post)
// UsernamePasswordAuthenticationFilter가 동작을함 but 폼로그인 disable해서 작동안하는상태였는데
//그래서 다시 등록을 해준다.
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // /login 요청시 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.trace("로그인 시도중 ...");

        //1.username,password 받아서
        try {

            ObjectMapper om = new ObjectMapper();//json데이터 파싱해줌
            User user = om.readValue(request.getInputStream(), User.class);
            log.trace("{}", user);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            // PrincipalDetailsService의 loadUserByUsername()함수가 실행됨.
            //authentication에는 내가 로그인한 정보가 담긴다.
            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);
            //=> 로그인이 되었다는 뜻.
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.trace("{}", principalDetails.getUser().getUsername());
            //authentication 객체가 session영역에 저장됨
            //리턴해주는 이유는 권한 관리를 security가 대신 해주기 떄문에 편하려고 하는 거임.
            //jwt토큰 사용시 세션 필요 없지만 권한 처리 사용하기 위해서 세션 사용.
            return authentication;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
