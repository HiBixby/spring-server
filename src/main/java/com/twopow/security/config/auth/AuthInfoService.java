package com.twopow.security.config.auth;

import com.twopow.security.jwt.JwtUtil;
import com.twopow.security.model.ErrorMessage;
import com.twopow.security.model.JwtTokens;
import com.twopow.security.model.Register;
import com.twopow.security.model.User;
import com.twopow.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthInfoService {
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> 회원주소저장(Register register, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String address = register.getAddress();
        System.out.println("프론트에서 받은 주소 : " + address);
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
    public ResponseEntity<?> JWT토큰재발급(JwtTokens jwtTokens, Authentication authentication) {
        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();
        if (jwtTokens.getRefreshToken().equals(user.getRefreshToken())) {
            JwtTokens newJwtTokens = JwtTokens.builder()
                    .accessToken(JwtUtil.CreateToken(user, JwtUtil.Minutes(30)))
                    .refreshToken(JwtUtil.CreateToken(null, JwtUtil.Days(14)))
                    .build();
            user.setRefreshToken(newJwtTokens.getRefreshToken());
            userRepository.save(user);
            return ResponseEntity.ok().body(newJwtTokens);

        }
        else{
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
}
