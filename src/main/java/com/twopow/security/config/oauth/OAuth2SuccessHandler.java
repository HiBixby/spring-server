package com.twopow.security.config.oauth;

import com.twopow.security.config.auth.PrincipalDetails;
import com.twopow.security.jwt.JwtUtil;
import com.twopow.security.model.User;
import com.twopow.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${server.react.scheme}")
    private String reactScheme;
    @Value("${server.react.host}")
    private String reactHost;
    @Value("${server.react.port}")
    private String reactPort;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();
        String accessToken = JwtUtil.CreateToken(user, JwtUtil.Minutes(30));
//        String refreshToken = JwtUtil.CreateToken(null,JwtUtil.Days(14));
        user.setRefreshToken("false");
        userRepository.save(user);
        String targetUrl = UriComponentsBuilder
                .newInstance()
                .scheme(reactScheme)
                .host(reactHost)
                .port(reactPort)
                .path("/oauth2/redirect")
                .queryParam("accessToken", accessToken)
                .encode()
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}