package com.fr1nge.myblog.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 我们使用JJWT生成/解析JWT令牌
 */
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public static String generateToken(String signingKey, String subject) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expireTime = new Date(nowMillis+60*60*1000);
        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)//储存的东西--用户名
                .setIssuedAt(now)//时间点
                .signWith(SignatureAlgorithm.HS256, signingKey)//加密方法和签名
                .setExpiration(expireTime);
        return builder.compact();
    }

    public static String getSubject(HttpServletRequest httpServletRequest, String jwtTokenName, String signingKey){
        //String token = httpServletRequest.getHeader(jwtTokenName);
        String token = (String) httpServletRequest.getSession().getAttribute(jwtTokenName);
        if(token == null || token.trim().length() == 0){
            return null;
        }else {
            try {
                return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody().getSubject();
            }catch (Exception e){
                logger.error(e.getMessage());
                return null;
            }
        }

    }
}
