package com.springws.app.service.impl;

import com.springws.app.entities.UserEntity;
import com.springws.app.repository.UserRepository;
import com.springws.app.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    final void testGetUser() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .firstName("John")
                .userId("uqiru98t9w7")
                .encryptedPassword("a89q7q7tq8q")
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        UserDto actual = userService.getUser("test@test.com");

        assertNotNull(actual);
        assertEquals("John", actual.getFirstName());
    }


}