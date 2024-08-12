package org.netmen;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EncoderTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void passwordTest(){
        System.out.println(passwordEncoder.encode("123"));

    }
}
