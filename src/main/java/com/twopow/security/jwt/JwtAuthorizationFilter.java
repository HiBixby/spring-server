package com.twopow.security.jwt;
//시큐리티가 filter 가지고있는데 그 필터중에 BasicAuthenticationFilter라는것이 있음.
//권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음
//만약에 권한이나 인증이 필요한 주소가 아니라면 이 필터를 안탄다.

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.twopow.security.config.auth.PrincipalDetails;
import com.twopow.security.model.User;
import com.twopow.security.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    //인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게됨
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("[JwtAuthorizationFilter] 인증이나 권한이 필요한 주소 요청이 됨.");

        String jwtHeader = request.getHeader("Authorization");
        log.info("[JwtAuthorizationFilter] jwtHeader: " + jwtHeader);
        //jwt토큰을 검증을해서 정상적인 사용자인지 확인
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
        //서명이 정상적으로 되었다면 username을 들고온다.

        Claims decodedJwtToken = JwtUtil.DecodeToken(jwtToken);
        // jwt 토큰이 올바르면 decodedUJwtToken을 잘 가져온다
        log.info("[JwtAuthorizationFilter] 디코드된 토큰 : "+decodedJwtToken);
        if (decodedJwtToken != null) {
            String username = String.valueOf(decodedJwtToken.get("username"));
            User userEntity = userRepository.findByUsername(username);
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

            //jwt토큰 서명을 통해서 서명이 정상이면 Authentication객체를 만들어준다.
            //가짜로 임의로 Authentication 객체를 만들어준다.(인증이 되어 username있으니까)
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities()); //password는 null로 그냥 넣어줬다. 세번째인자는 권한


            //강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장.
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }
    }
}
