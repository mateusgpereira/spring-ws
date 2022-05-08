package com.springws.app.controller;

import com.springws.app.service.impl.UserServiceImpl;
import com.springws.app.shared.dto.AddressDto;
import com.springws.app.shared.dto.UserDto;
import com.springws.app.ui.model.response.UserRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    UserServiceImpl userService;

    @InjectMocks
    UserController userController;

    UserDto userDto;

    final String USER_ID = "bahidf89659q";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDto = UserDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("test@email.com")
                .emailVerificationStatus(false)
                .emailVerificationToken(null)
                .userId(USER_ID)
                .addresses(getAddressesDto())
                .encryptedPassword("ufai7f9a7f")
                .build();
    }

    @Test
    void getUsers() {
        when(userService.getUserByUserId(anyString())).thenReturn(userDto);

        UserRest userRest = userController.getUsers(USER_ID);

        assertNotNull(userRest);
        assertEquals(USER_ID, userRest.getUserId());
        assertEquals(userDto.getFirstName(), userRest.getFirstName());
        assertEquals(userDto.getLastName(), userRest.getLastName());
        assertTrue(userDto.getAddresses().size() == userRest.getAddresses().size());
    }

    private List<AddressDto> getAddressesDto() {
        AddressDto shipping = AddressDto.builder()
                .type("shipping")
                .city("Vancouver")
                .country("Canada")
                .postalCode("ABC123")
                .streetName("Street name")
                .build();

        AddressDto billing = AddressDto.builder()
                .type("billing")
                .city("Vancouver")
                .country("Canada")
                .postalCode("ABC123")
                .streetName("Street name")
                .build();

        return Arrays.asList(shipping, billing);
    }
}