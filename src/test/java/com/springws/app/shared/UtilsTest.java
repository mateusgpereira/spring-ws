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
    void hasTokenExpired() {
    }
}