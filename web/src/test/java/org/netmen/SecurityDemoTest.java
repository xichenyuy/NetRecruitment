package org.netmen;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

@SpringBootTest
public class SecurityDemoTest {
    @Test
    void testPassword(){
        PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String result = encoder.encode("password");
        System.out.println(result);

        Assert.isTrue(encoder.matches("password", result), "密码不一致");
    }
}
