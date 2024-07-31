package org.netmen.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    //token生成
    @Test
    public void testGen() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("username", "yang");
        String token = JWT.create()
                .withAudience("user", String.valueOf(claims))
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12))    //过期时间
                .sign(Algorithm.HMAC256("netmen")); //指定加密算法 配置密钥
        System.out.println(token);
    }

    //token验证
    @Test
    public void testParse(){
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJhdWQiOlsidXNlciIsIntpZD0xLCB1c2VybmFtZT15YW5nfSJdLCJleHAiOjE3MjI0NTY5Mzl9" +
                ".-RTHEH8eJ7Fd5HOEPrHP--07sy0pRgdYQbBv8iZ8A2A";
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("netmen")).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        Map<String, Claim> claims = decodedJWT.getClaims();
        System.out.println(claims);
    }
}

