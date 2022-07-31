package com.twopow.security.controller;

import com.twopow.security.config.auth.AuthInfoService;
import com.twopow.security.model.JwtTokens;
import com.twopow.security.model.Register;
import com.twopow.security.model.VerifyJwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
    public ResponseEntity<?> reissue(JwtTokens jwtTokens) {
        return authInfoService.JWT토큰재발급(jwtTokens);
    }

    @PostMapping("/auth/validity")
    public ResponseEntity<?> CheckTokenExpired(@RequestBody VerifyJwt jwtTokens) {
        return authInfoService.만료되지않은토큰인지검증한다(jwtTokens);
    }

    @GetMapping("/auth/refresh-token")
    public ResponseEntity<?> SendRefreshToken(Authentication authentication) {
        return authInfoService.리프래시토큰을발급해준다(authentication);
    }
}