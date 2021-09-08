package com.fr1nge.myblog.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * JJWT生成/解析JWT令牌
 */
@Slf4j
public class JwtUtil {

    public static String generateToken(String signingKey, String subject) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expireTime = new Date(nowMillis + 15 * 60 * 1000);
        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)//储存的东西--用户名
                .setIssuedAt(now)//时间点
                .signWith(SignatureAlgorithm.HS256, signingKey)//加密方法和签名
                .setExpiration(expireTime);
        log.info("token expireTime = " + expireTime);
        log.info("token = " + builder.compact());
        return builder.compact();
    }

    public static String getSubject(HttpServletRequest httpServletRequest, String jwtTokenName, String signingKey) {
        String token = (String) httpServletRequest.getSession().getAttribute(jwtTokenName);
        if (token == null || token.trim().length() == 0) {
            return null;
        } else {
            try {
                return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody().getSubject();
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
            }
        }

    }

}
