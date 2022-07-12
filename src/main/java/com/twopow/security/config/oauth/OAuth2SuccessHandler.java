package com.twopow.security.config.oauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.twopow.security.config.auth.PrincipalDetails;
import com.twopow.security.model.JoinedUser;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

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
        ObjectMapper objectMapper = new ObjectMapper();
        String targetUrl;
        PrincipalDetails principalDetails=(PrincipalDetails)authentication.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject("2-pow Token")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000) * 30)) //30min
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("2powTeam"));




        targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect?userToken="+jwtToken)
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);


    }
}