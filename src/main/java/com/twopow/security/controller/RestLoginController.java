package com.twopow.security.controller;

import com.twopow.security.config.auth.AuthInfoService;
import com.twopow.security.config.auth.PrincipalDetails;
import com.twopow.security.model.JoinedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class RestLoginController {
    private final AuthInfoService authInfoService;

    @GetMapping("/hello")
    public String Hello() {
        return "hello";
    }

    @GetMapping("/auth/info")
    public ResponseEntity<?> authRole(HttpServletResponse response, Authentication authentication){
        JoinedUser joinedUser;
        //HttpHeaders headers = new HttpHeaders();
        if (authentication != null) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            joinedUser = JoinedUser.builder()
                    .username(principalDetails.getUser().getName())
                    .email(principalDetails.getUser().getEmail())
                    .picture(principalDetails.getUser().getPicture())
                    .address(principalDetails.getUser().getAddress())
                    .build();
            return ResponseEntity.ok().body(joinedUser);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(HttpServletRequest request, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String address = request.getParameter("address");
        authInfoService.주소저장(principalDetails, address);
        return ResponseEntity.ok().body(address);
    }
}