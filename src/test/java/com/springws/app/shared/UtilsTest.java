package com.springws.app.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

    @Autowired
    Utils utils;

    @BeforeEach
    void setUp() {
    }

    @Test
    void generateUserId() {
        String userIdOne = utils.generateUserId(30);
        String userIdTwo = utils.generateUserId(30);

        assertNotNull(userIdOne);
        assertTrue(userIdOne.length() == 30);
        assertTrue(!userIdOne.equalsIgnoreCase(userIdTwo));
    }

    @Test
    void hasTokenNotExpired() {
        String token = utils.generateEmailVerificationToken("df98f7a9q69");
        assertNotNull(token);

        boolean hasTokenExpired = Utils.hasTokenExpired(token);
        assertFalse(hasTokenExpired);
    }

    @Test
    void shouldTokenBeExpired() {
        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXRldXNnb25jYWx2ZXNwZXJlaXJhQGdtYWlsLmNvbSIsImV4cCI6MTY1MjAxOTI2Nn0.a3Xe-1fSa5tSBgB_wXRAxD__z0Zw5RUyzwqLSk-jlYlstz7BOw4xvb8306vg13xCJH20kd6i3o4hQtowH5bRKw";
        boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);
        assertTrue(hasTokenExpired);
    }
}