package com.twopow.security.config.auth;

import com.twopow.security.jwt.JwtUtil;
import com.twopow.security.model.*;
import com.twopow.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthInfoService {
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> 회원정보가져오기(Authentication authentication) {
        JoinedUser joinedUser;
        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();
        joinedUser = JoinedUser.builder()
                .username(user.getName())
                .email(user.getEmail())
                .picture(user.getPicture())
                .address(user.getAddress())
                .build();
        return ResponseEntity.ok().body(joinedUser);
    }

    @Transactional
    public ResponseEntity<?> 회원주소저장(Register register, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String address = register.getAddress();
        log.trace("[Register] 프론트에서 받은 주소 : {}", address);
        Optional<User> userOptional;
        User user;
        userOptional = userRepository.findByProviderAndProviderId(principalDetails.getUser().getProvider(), principalDetails.getUser().getProviderId());
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (user.getAddress() == null) {
                user.setAddress(address);
                userRepository.save(user);
                return ResponseEntity.ok().body(address);
            } else {
                ErrorMessage errorMessage = ErrorMessage.builder()
                        .status(400)
                        .error("Bad Request")
                        .message("User address already exists")
                        .path("/register")
                        .build();

                return ResponseEntity.badRequest().body(errorMessage);
            }
        }
        return null;
    }

    @Transactional
    public ResponseEntity<?> JWT토큰재발급(HttpServletRequest request, JwtTokens jwtTokens) {
        String oldAccessToken = jwtTokens.getAccessToken();
        String oldRefreshToken = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie c: cookies){
            if (c.getName().equals("refreshToken")){
                oldRefreshToken = c.getValue();
                log.trace("[Reissue] refreshToken 쿠키를 찾았습니다!");
                break;
            }
        }
        
        if (oldRefreshToken==null){
            oldRefreshToken=jwtTokens.getRefreshToken();
        }

        log.trace("[Reissue] Front-end 에서 Back-end 로 보낸 Access Token : {}", oldAccessToken);
        log.trace("[Reissue] Front-end 에서 Back-end 로 보낸 Refresh Token : {}", oldRefreshToken);

        String username = JwtUtil.getUsernameFromJWT(oldAccessToken);
        User user = userRepository.findByUsername(username);

        if ((oldRefreshToken).equals(user.getRefreshToken()) && JwtUtil.DecodeToken(oldRefreshToken) != null) {
            String newAccessToken = JwtUtil.CreateToken(user, JwtUtil.Minutes(30));
            String newRefreshToken = JwtUtil.CreateToken(null, JwtUtil.Days(14));
            JwtTokens newJwtTokens = JwtTokens.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
            user.setRefreshToken(newRefreshToken);
            userRepository.save(user);

            log.trace("[Reissue] Back-end 에서 재발급한 Access Token : {}", newAccessToken);
            log.trace("[Reissue] Back-end 에서 재발급한 Refresh Token : {}", newRefreshToken);

            return ResponseEntity.ok().body(newJwtTokens);

        } else {

            log.error("[Reissue] Front-End에서 받은 Refresh Token이 DB에 저장된 Refresh Token과 일치하지 않음");

            ErrorMessage errorMessage = ErrorMessage.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status(400)
                    .error("Bad Request")
                    .message("Invalid refresh token")
                    .path("/auth/reissue")
                    .build();
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @Transactional
    public ResponseEntity<?> 만료되지않은토큰인지검증한다(VerifyJwt verifyJwt) {
        String jwtToken = verifyJwt.getJwtToken();
        if (!JwtUtil.isExpiredJwt(jwtToken)) {
            VerifyJwt result = VerifyJwt.builder().jwtToken(jwtToken).expired(false).build();
            return ResponseEntity.ok().body(result);
        } else {
            VerifyJwt result = VerifyJwt.builder().jwtToken(jwtToken).expired(true).build();
            return ResponseEntity.status(401).body(result);
        }
    }
    @Transactional
    public ResponseEntity<?> 리프래시토큰을발급해준다(Authentication authentication) {
        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();
        if (user != null && Objects.equals(user.getRefreshToken(), "false")) {
            String refreshToken = JwtUtil.CreateToken(null, JwtUtil.Days(14));
            log.trace("발급된 refresh token : {}", refreshToken);
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
            JwtTokens jwtTokens = JwtTokens.builder().refreshToken(refreshToken).build();
            return ResponseEntity.ok().body(jwtTokens);
        } else {
            log.error("에러 발생");
            ErrorMessage errorMessage = ErrorMessage.builder()
                    .timestamp(new Timestamp(System.currentTimeMillis()))
                    .status(401)
                    .error("UNAUTHORIZED")
                    .message("")
                    .path("/")
                    .build();
            return ResponseEntity.status(401).body(errorMessage);
        }
    }
}
