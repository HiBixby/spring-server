package com.twopow.security.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.twopow.security.config.auth.PrincipalDetails;
import com.twopow.security.model.JoinedUser;
import org.hibernate.mapping.Join;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class RestLoginController {
//    @GetMapping("/oauth2/redirect")
//    public JoinedUser oauthRedirect(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth){
//
//        ObjectMapper objectMapper=new ObjectMapper();
//        PrincipalDetails principalDetails=(PrincipalDetails)authentication.getPrincipal();
//
//        String email = principalDetails.getUser().getEmail();
//        String name = principalDetails.getUser().getName();
//        String picture = principalDetails.getUser().getPicture();
//        String encodedname = null;
//        try {
//            encodedname = URLEncoder.encode(name, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//
//        String jwtToken = JWT.create()
//                .withSubject("2-pow Token")
//                .withExpiresAt(new Date(System.currentTimeMillis() + (60000) * 30)) //30min
//                .withClaim("id", principalDetails.getUser().getId())
//                .withClaim("username", principalDetails.getUser().getUsername())
//                .sign(Algorithm.HMAC512("2powTeam"));
//
//        JoinedUser joinedUser=new JoinedUser();
//        joinedUser.setUserToken(jwtToken);
//        joinedUser.setEmail(email);
//        joinedUser.setUsername(encodedname);
//        joinedUser.setProfileImageUrl(picture);
//
//        return joinedUser;
//    }
    @GetMapping("/hello")
    public String Hello(){
        return "hello";
    }
}
