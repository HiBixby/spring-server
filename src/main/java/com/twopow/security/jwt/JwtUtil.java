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

    public static Date Minutes(int minutes) {
        return new Date (System.currentTimeMillis()+(6000L) * minutes);
    }

    public static Date Days(int days){
        return new Date (System.currentTimeMillis()+(6000L *60*24)*days);
    }

    public static String CreateToken(User user, Date expiresAt) {
        String createdToken;
        if (user != null){
            String id = String.valueOf(user.getId());
            String username = user.getUsername();

            createdToken = JWT.create()
                    .withSubject(subject)
                    .withExpiresAt(expiresAt)
                    .withClaim("id", id)
                    .withClaim("username", username)
                    .sign(Algorithm.HMAC512(secretKey));
            System.out.println("JWT access token : "+createdToken);
        }
        else{
            createdToken = JWT.create()
                    .withSubject(subject)
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC512(secretKey));
            System.out.println("JWT refresh token : "+createdToken);
        }

        return createdToken;
    }

    public static DecodedJWT DecodeToken(String encodedJwtToken) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC512(secretKey)).build()
                .verify(encodedJwtToken);
    }
}