package com.twopow.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twopow.security.config.auth.PrincipalDetails;
import com.twopow.security.model.User;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    // /login 요청시 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter: 로그인 시도중");

        //1.username,password 받아서
        try {
//            BufferedReader br=request.getReader();
//            String input=null;
//            while((input=br.readLine())!=null){
//                System.out.println(input);
//            }
//            System.out.println(request.getInputStream().toString());
            ObjectMapper om=new ObjectMapper();//json데이터 파싱해줌
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            UsernamePasswordAuthenticationToken authenticationToken=
                    new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
            // PrincipalDetailsService의 loadUserByUsername()함수가 실행됨.
            //authentication에는 내가 로그인한 정보가 담긴다.
            Authentication authentication=
                    authenticationManager.authenticate(authenticationToken);
            //=> 로그인이 되었다는 뜻.
            PrincipalDetails principalDetails=(PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUser().getUsername());
            //authentication 객체가 session영역에 저장됨
            //리턴해주는 이유는 권한 관리를 security가 대신 해주기 떄문에 편하려고 하는 거임.
            //jwt토큰 사용시 세션 필요 없지만 권한 처리 사용하기 위해서 세션 사용.
            return authentication;


        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }


//    //attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행된다.
//    //JWT 토큰을 만들어서 request 요청한 사용자에게 jwt 만들어주면된다.
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        System.out.println("successfulAuthentication 실행됨: 인증이 완료되었다는 뜻");
//        PrincipalDetails principalDetails=(PrincipalDetails) authResult.getPrincipal();
//
//        String jwtToken= JWT.create()
//                .withSubject("2-pow Token")
//                .withExpiresAt(new Date(System.currentTimeMillis()+(60000) *30)) //30min
//                .withClaim("id",principalDetails.getUser().getId())
//                .withClaim("username",principalDetails.getUser().getUsername())
//                                .sign(Algorithm.HMAC512("2powTeam"));
//        response.addHeader("Authhorization","Bearer "+jwtToken);
//    }
}
