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
import java.lang.reflect.Array;
import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
public class RestApiController {
    private final AuthInfoService authInfoService;

    @GetMapping("/hello")
    public String Hello() {
        return "hello";
    }

    @GetMapping("/auth/info")
    public ResponseEntity<?> authRole(HttpServletResponse response, Authentication authentication) {
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
        String result = authInfoService.주소저장(request, authentication);
        return ResponseEntity.ok().body(result);
    }
}