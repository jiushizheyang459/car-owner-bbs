package com.ori;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordEncoderTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testCreatePassword() {
        String password = "000000";
        String encodePassword = passwordEncoder.encode(password);
        System.out.println((encodePassword));
    }
}
