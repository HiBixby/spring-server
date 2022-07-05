package com.twopow.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import java.time.ZonedDateTime;
import java.util.Date;

public class JWTUtil {
    private String secretKey="이에이승팀테스트용jwt비밀키";
    //1month
    private long expire= 60*24*30;

    public String generateToken(String content) throws Exception{
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().
                        plusMinutes(expire).toInstant()))
                .claim("sub",content)
                .signWith(SignatureAlgorithm.HS256,secretKey.getBytes("UTF-8")).compact();

    }
    public String validateAndExtract(String tokenStr)throws Exception{
        String contentValue = null;
        try{
            DefaultJws defaultJws = (DefaultJws) Jwts.parser()
                    .setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(tokenStr);

            System.out.println(defaultJws);
            System.out.println(defaultJws.getBody().getClass());
            DefaultClaims claims=(DefaultClaims) defaultJws.getBody();
            System.out.println("-----------------------------");
            contentValue = claims.getSubject();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            contentValue=null;
        }
        return contentValue;
    }
}
