package com.twopow.security.controller;

import com.twopow.security.config.auth.AuthInfoService;
import com.twopow.security.config.auth.PrincipalDetails;
import com.twopow.security.model.JoinedUser;
import com.twopow.security.model.JwtTokens;
import com.twopow.security.model.Register;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class RestApiController {
    private final AuthInfoService authInfoService;

    @GetMapping("/auth/info")
    public ResponseEntity<?> authInfo(Authentication authentication) {
        return authInfoService.회원정보가져오기(authentication);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register register, Authentication authentication) {
        return authInfoService.회원주소저장(register, authentication);
    }

    @PostMapping("/auth/reissue")
    public ResponseEntity<?> reissue(@RequestBody JwtTokens jwtTokens, Authentication authentication) {
        return authInfoService.JWT토큰재발급(jwtTokens, authentication);
    }
}