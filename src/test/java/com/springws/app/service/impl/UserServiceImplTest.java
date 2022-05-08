package com.springws.app.service.impl;

import com.springws.app.entities.AddressEntity;
import com.springws.app.entities.UserEntity;
import com.springws.app.repository.UserRepository;
import com.springws.app.service.EmailVerificationService;
import com.springws.app.shared.Utils;
import com.springws.app.shared.dto.AddressDto;
import com.springws.app.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    EmailVerificationService emailVerificationService;

    @InjectMocks
    UserServiceImpl userService;

    UserEntity userEntity;

    String userId = "ahhfuaif89258";
    String addressId = "ahhfuaif89258";
    String encryptedPassword = "78g656q928g";

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        userEntity = UserEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .userId(userId)
                .encryptedPassword(encryptedPassword)
                .email("test@test.com")
                .emailVerificationToken("dhudiudha533")
                .addresses(getAddressesEntities())
                .build();


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

    @Test
    final void shouldThrowUsernameNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUser("test@email.com");
        });
    }

    @Test
    final void shouldCreateUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn(addressId);
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto userDto = UserDto.builder()
                .addresses(this.getAddressesDto())
                .password("1234567")
                .build();

        UserDto actual = userService.createUser(userDto);
        assertNotNull(actual);
        assertEquals(userEntity.getFirstName(), actual.getFirstName());
        assertEquals(userEntity.getLastName(), actual.getLastName());
        assertNotNull(actual.getUserId());
        assertEquals(userEntity.getAddresses().size(), actual.getAddresses().size());

        verify(utils, times(2)).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("1234567");
        verify(userRepository, times(1)).save(any(UserEntity.class));
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

    private List<AddressEntity> getAddressesEntities() {
        List<AddressDto> addresses = getAddressesDto();
        Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
        return new ModelMapper().map(addresses, listType);
    }


}