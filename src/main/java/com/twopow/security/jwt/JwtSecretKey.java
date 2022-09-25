package com.twopow.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtSecretKey {
    public static String secretKey;
    @Value("${jwt.secret-key}")
    public void setSecretKey(String key){
        secretKey = key;
    }

}
