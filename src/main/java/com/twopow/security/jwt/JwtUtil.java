package com.twopow.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.twopow.security.model.User;

import java.util.Date;

public class JwtUtil {

    private static final String subject = "이에이승";
    static String secretKey = "이에이승팀의샘플비밀키입니다.";

    public static Date ConvertDurationToExpiration(int min) {
        return new Date(System.currentTimeMillis() + (6000L) * min);
    }

    public static String CreateToken(User user, int durationMinutes) {
        String id= String.valueOf(user.getId());
        String username=user.getUsername();
        Date expirationTime = ConvertDurationToExpiration(durationMinutes);

        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(expirationTime)
                .withClaim("id", id)
                .withClaim("username", username)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public static String decodeToken(String encodedJwtToken){
        try{
            return JWT.require(Algorithm.HMAC512(secretKey)).build()
                    .verify(encodedJwtToken)
                    .getClaim("username")
                    .asString();

        } catch (JWTVerificationException e) {
            String cause = String.valueOf(e.getCause());
            System.out.println("토큰 검증 오류 사유 : "+cause);

        }
        return null;
    }
}