package org.netmen.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    private static final String KEY = "test";
    public static final Long TTL = 1000L * 60 * 60;

    //生成id
    private static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }


    /**
     * 生成jwt
     * @param subject
     * @param ttlMills
     * @return
     */
    public static String createJwt(String subject, Long ttlMills){
        JwtBuilder builder = getJwtBuilder(subject, ttlMills, getUUID());
        return builder.compact();
    }

    /**
     * 生成jwt
     * @param id
     * @param subject
     * @param ttlMills
     * @return
     */
    public static String createJwt(String id, String subject, Long ttlMills){
        JwtBuilder builder = getJwtBuilder(subject, ttlMills, id);
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMills, String uuid){
        if(ttlMills == null){   //如果没有设置过期时间 使用默认过期时间
            ttlMills = JwtUtil.TTL;
        }
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setId(uuid)
                .setSubject(subject)
                .setIssuer("netmen")  //签发者
                .setIssuedAt(new Date(currentTimeMillis))   //签发时间
                .signWith(SignatureAlgorithm.HS256, JwtUtil.KEY)  //加密方式和密钥
                .setExpiration(new Date(currentTimeMillis + ttlMills)); //过期时间
    }

    /**
     * 解析jwt
     * @param jwt
     * @return
     */
    public static Claims parseJwt(String jwt) throws Exception {
        return Jwts.parser()
                .setSigningKey(JwtUtil.KEY)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
