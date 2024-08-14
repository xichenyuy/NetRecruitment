package org.netmen;


import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.netmen.common.utils.JwtUtil;
import org.springframework.boot.test.context.SpringBootTest;

public class JwtTest {
    @Test
    public void createJwt(){
        String jwt = JwtUtil.createJwt("10086", 1000L * 60 * 60);
        System.out.println(jwt);
    }

    @Test
    public void parseJwt() throws Exception {
        Claims claims = JwtUtil.parseJwt("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0NGFjZTY3ZDJmZjA0NzAzODYyYzk2YThiOGVlNDJhYiIsInN1YiI6IjEwMDg2IiwiaXNzIjoibmV0bWVuIiwiaWF0IjoxNzIzNjAyNjczLCJleHAiOjE3MjM2MDYyNzN9.MLM7VQmKEKi4ZdSbpB7kAuZgmeZW79hHsDYxl0Nk0wg");
        System.out.println(claims);
        System.out.println(claims.getId());
    }
}

