package com.twopow.security.jwt;

import com.twopow.security.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.management.StringValueExp;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Slf4j
public class JwtUtil {

    private static final String subject = "이에이승";
    static String secretKey = "이에이승팀의샘플비밀키입니다.";

    public static Date Minutes(int minutes) {
        return new Date(System.currentTimeMillis()+1000*60L*minutes);
    }

    public static Date Days(int days) {
        return new Date(System.currentTimeMillis()+1000*60L*60L*24L*days);
    }

    public static String CreateToken(User user, Date expiresAt) {
        String createdToken;
        if (user != null) {
            String id = String.valueOf(user.getId());
            String username = user.getUsername();

            createdToken = Jwts.builder()
                    .setSubject(subject)
                    .setId(id)
                    .claim("username", username)
                    .setExpiration(expiresAt)
                    .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                    .compact();

            log.trace("JWT access token : {}",createdToken);
        } else {
            createdToken = Jwts.builder()
                    .setSubject(subject).setExpiration(expiresAt).signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8)).compact();
            log.trace("JWT refresh token : {}",createdToken);
        }

        return createdToken;
    }

    public static Claims DecodeToken(String encodedJwtToken) {
        return Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(encodedJwtToken).getBody();
    }

    public static String getUsernameFromJWT(String expiredAccessToken) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(expiredAccessToken).getBody();
            return claims.get("username",String.class);
        } catch (ExpiredJwtException e) {
            String username = e.getClaims().get("username",String.class);
            log.trace("username from expired JWT: {}", username);
            return username;
        } catch (Exception ignored) {

        }
        return null;
    }

    public static boolean isExpiredJwt(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(jwtToken).getBody();
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}