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
        Claims claims = JwtUtil.parseJwt("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyMmI4NjU2M2NkNTI0YjBlOTQyMWY4MzNlZDAyMDY0ZCIsInN1YiI6IjEwMDg2IiwiaXNzIjoibmV0bWVuIiwiaWF0IjoxNzIzMDE3MTMwLCJleHAiOjE3MjMwMjA3MzB9.M5JLJMX3UX-U64C6w9lbxg-Q-jmeTwY7Y88zVFnUYr8");
        System.out.println(claims);
        System.out.println(claims.getId());
    }
}

