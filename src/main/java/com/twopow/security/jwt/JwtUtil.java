package com.twopow.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.twopow.security.model.User;

import java.util.Date;

public class JwtUtil {

    private static final String subject = "이에이승";
    static String secretKey = "이에이승팀의샘플비밀키입니다.";

    public static Date ConvertDurationToExpiration(int min) {
        return new Date(System.currentTimeMillis() + (6000L) * min);
    }

    public static String CreateToken(User user, int durationMinutes) {
        String id = String.valueOf(user.getId());
        String username = user.getUsername();
        Date expirationTime = ConvertDurationToExpiration(durationMinutes);
        String createdToken = JWT.create()
                .withSubject(subject)
                .withExpiresAt(expirationTime)
                .withClaim("id", id)
                .withClaim("username", username)
                .sign(Algorithm.HMAC512(secretKey));
        System.out.println("생성된 JWT 토큰 : "+createdToken);
        return createdToken;
    }

    public static DecodedJWT decodeToken(String encodedJwtToken) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC512(secretKey)).build()
                .verify(encodedJwtToken);
    }
}