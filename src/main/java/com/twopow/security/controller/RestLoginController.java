//package com.twopow.security.controller;
//
//import com.twopow.security.config.auth.PrincipalDetails;
//import com.twopow.security.model.JoinedUser;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//@CrossOrigin(origins = "http://localhost:3000")
//@RestController
//public class RestLoginController {
//    @RequestMapping("/oauth/redirect")
//    public JoinedUser Nowij(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth){
//        PrincipalDetails principalDetails=(PrincipalDetails)authentication.getPrincipal();
//        JoinedUser joinedUser=new JoinedUser();
//        joinedUser.setUserToken();
//        joinedUser.setEmail(principalDetails.getUser().getEmail());
//        joinedUser.setUsername(principalDetails.getUser().getName());
//        joinedUser.setProfileImageUrl(principalDetails.getUser().getPicture());
//
//
//
//        return "";
//    }
//}
